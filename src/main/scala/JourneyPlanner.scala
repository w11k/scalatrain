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

import util.Logging
import scala.collection.immutable.Seq

class JourneyPlanner(trains: Set[Train]) extends Logging {
  require(trains != null, "trains must not be null!")
  logger.debug("Initialized with the following %s trains:\n%s".format(trains.size, trains))

  val stations: Set[Station] =
    trains flatMap { _.stations }

  def trains(station: Station): Set[Train] = {
    require(station != null, "station must not be null!")
    trains filter { _.stations contains station }
  }

  def departures(station: Station): Set[(Time, Train)] = {
    require(station != null, "station must not be null!")
//    trains flatMap { train =>
//      train.schedule filter { timeAndStation =>
//        timeAndStation._2 == station
//      } map { timeAndStation =>
//        timeAndStation._1 -> train
//      }
//    }
    for {
      train <- trains
      (time, station2) <- train.schedule if (station2 == station)
    } yield time -> train
  }

  def isShortTrip(from: Station, to: Station): Boolean = {
    require(from != null, "from must not be null!")
    require(to != null, "to must not be null!")
    trains exists {
      _.stations dropWhile { from != } match {
        case Seq(`from`, _, `to`, _*) => true
        case Seq(`from`, `to`, _*) => true
        case _ => false
      }
    }
  }

  def connections(from: Station, to: Station, departureTime: Time): Set[Seq[Hop]] = {
    require(from != null, "from must not be null!")
    require(to != null, "to must not be null!")
    require(from != to, "from and to must be different!")
    require(departureTime != null, "departureTime must not be null!")

    def connections(soFar: Seq[Hop], to: Station): Set[Seq[Hop]] = {
      val currentStation = soFar.last.to
      if (currentStation == to)
        Set(soFar)
      else {
        val soFarStations = soFar.head.from +: (soFar map { _.to })
        val nextHops =
          hopsFrom(currentStation) filter { hop =>
            !(soFarStations contains hop.to) && (hop.departureTime >= soFar.last.arrivalTime)
          }
        nextHops flatMap { hop => connections(soFar :+ hop, to) }
      }
    }

    hopsFrom.getOrElse(from, Set.empty) filter {
      _.departureTime >= departureTime
    } flatMap {
      hop => connections(Vector(hop), to)
    }
  }

  private val hopsFrom = {
    val hops = for {
      train <- trains
      (from, to) <- train.consecutiveStations
    } yield Hop(from, to, train)
    hops groupBy { _.from }
  }
}

trait JourneyPlannerContext {

  lazy val journeyPlanner: JourneyPlanner =
    new JourneyPlanner(trains)

  protected def trains: Set[Train]
}
