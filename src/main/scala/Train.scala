/*
 * Copyright 2011 Weigle Wilczek GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.scalatrain

import util.{ hasNoDuplicates, isIncreasing }
import scala.collection.immutable.Seq

case class Train(info: TrainInfo, schedule: Seq[(Time, Station)]) {
  require(info != null, "info must not be null!")
  require(schedule != null, "schedule must not be null!")
  require(schedule.size >= 2, "schedule must have at least two stops!")
  require(isIncreasing(schedule map { _._1 }), "schedule must contain monotonically increasing times!")
  require(hasNoDuplicates(schedule map { _._2 }), "schedule must not contain duplicate stations!")

  val stations: Seq[Station] =
    schedule map { _._2 }

  override def toString =
    info match {
      case Ice(number, true) => "ICE %s (WIFI)" format number
      case Ice(number, _) => "ICE %s" format number
      case Re(number) => "RE %s" format number
      case Brb(number) => "BRB %s" format number
    }

  private[scalatrain] val consecutiveStations: Seq[(Station, Station)] =
    stations.init zip stations.tail map {
      case (from, to) => from -> to
    }

  private[scalatrain] val departureTimeFrom: Map[Station, Time] =
    schedule map { _.swap } toMap
}

object Station {

  implicit def stringToStation(s: String): Station =
    Station(s)
}

case class Station(name: String) {
  require(name != null, "name must not be null!")
}

sealed abstract class TrainInfo {

  def number: String
}

case class Ice(number: String, hasWifi: Boolean = false) extends TrainInfo

case class Re(number: String) extends TrainInfo

case class Brb(number: String) extends TrainInfo

case class Hop(from: Station, to: Station, train: Train) {
  require(from != null, "from must not be null!")
  require(to != null, "to must not be null!")
  require(from != to, "from and to must not be equal!")
  require(train != null, "train must not be null!")
  require(train.consecutiveStations contains from -> to, "train must contain hop from -> to!")

  def departureTime: Time =
    train.departureTimeFrom(from)

  def arrivalTime: Time =
    train.departureTimeFrom(to)
}
