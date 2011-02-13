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

import org.specs.Specification

class JourneyPlannerSpec extends Specification {

  "Creating a JourneyPlanner" should {

    "throw an IllegalArgumentException for null trains" in {
      new JourneyPlanner(null) must throwA[IllegalArgumentException]
    }
  }

  "Calling stations" should {
    val journeyPlanner = new JourneyPlanner(Set(train1, train2))

    "return the correct stations" in {
      journeyPlanner.stations mustEqual Set(stationA, stationB, stationC, stationD)
    }
  }

  "Calling trains" should {
    val journeyPlanner = new JourneyPlanner(Set(train1, train2))

    "throw an IllegalArgumentException for a null station" in {
      journeyPlanner trains null must throwA[IllegalArgumentException]
    }

    "return the correct result" in {
      journeyPlanner trains stationA mustEqual Set(train1, train2)
      journeyPlanner trains stationB mustEqual Set(train1, train2)
      journeyPlanner trains stationC mustEqual Set(train1)
      journeyPlanner trains stationD mustEqual Set(train2)
      journeyPlanner trains Station("") mustEqual Set.empty
    }
  }

  "Calling departures" should {
    val journeyPlanner = new JourneyPlanner(Set(train1, train2))

    "throw an IllegalArgumentException for a null station" in {
      journeyPlanner departures null must throwA[IllegalArgumentException]
    }

    "return the correct result" in {
      journeyPlanner departures stationA mustEqual Set(Time(0, 0) -> train1, Time(2, 2) -> train2)
      journeyPlanner departures stationB mustEqual Set(Time(1, 1) -> train1, Time(1, 1) -> train2)
      journeyPlanner departures stationC mustEqual Set(Time(2, 2) -> train1)
      journeyPlanner departures stationD mustEqual Set(Time(0, 0) -> train2)
      journeyPlanner departures Station("") mustEqual Set.empty
    }
  }

  "Calling isShortTrip" should {
    val journeyPlanner = new JourneyPlanner(Set(train1, train2))

    "throw an IllegalArgumentException for a null from" in {
      journeyPlanner.isShortTrip(null, stationA) must throwA[IllegalArgumentException]
    }

    "throw an IllegalArgumentException for a null to" in {
      journeyPlanner.isShortTrip(stationD, null) must throwA[IllegalArgumentException]
    }

    "return the correct result" in {
      journeyPlanner.isShortTrip(stationA, stationB) mustEqual true
      journeyPlanner.isShortTrip(stationA, stationC) mustEqual true
      journeyPlanner.isShortTrip(stationC, stationA) mustEqual false
      journeyPlanner.isShortTrip(Station("X"), stationA) mustEqual false
      journeyPlanner.isShortTrip(stationA, Station("X")) mustEqual false
    }
  }

  private lazy val train1 = new Train(Ice("720"), schedule1)

  private lazy val train2 = new Train(Ice("722"), schedule2)

  private lazy val schedule1 =
    List(
      Time(0, 0) -> stationA,
      Time(1, 1) -> stationB,
      Time(2, 2) -> stationC)

  private lazy val schedule2 =
    List(
      Time(0, 0) -> stationD,
      Time(1, 1) -> stationB,
      Time(2, 2) -> stationA)

  private lazy val stationA = Station("A")

  private lazy val stationB = Station("B")

  private lazy val stationC = Station("C")

  private lazy val stationD = Station("D")
}
