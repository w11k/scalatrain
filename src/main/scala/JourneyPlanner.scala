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

class JourneyPlanner(trains: Set[Train]) {
  require(trains != null, "trains must not be null!")

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
      timeAndStation <- train.schedule if (timeAndStation._2 == station)
    } yield timeAndStation._1 -> train
  }
}
