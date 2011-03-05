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

  "Calling connections" should {
    val journeyPlanner = new JourneyPlanner(Set(train1, train2))

    "throw an IllegalArgumentException for a null from" in {
      journeyPlanner.connections(null, stationB, Time(0)) must throwA[IllegalArgumentException]
    }

    "throw an IllegalArgumentException for a null to" in {
      journeyPlanner.connections(stationA, null, Time(0)) must throwA[IllegalArgumentException]
    }

    "throw an IllegalArgumentException for identical from and to" in {
      journeyPlanner.connections(stationA, stationA, Time(0)) must throwA[IllegalArgumentException]
    }

    "throw an IllegalArgumentException for a null departureTime" in {
      journeyPlanner.connections(stationA, stationB, null) must throwA[IllegalArgumentException]
    }

    "return no connections for a unknown from or to" in {
      journeyPlanner.connections(Station("C"), stationB, Time(10)) must beEmpty
      journeyPlanner.connections(stationA, Station("C"), Time(10)) must beEmpty
    }

    "return no connections for a too early departureTime" in {
      journeyPlanner.connections(stationA, stationB, Time(10)) must beEmpty
    }

    "return one connections for the trivial setup" in {
      journeyPlanner.connections(stationA, stationC, Time()) mustEqual Set(Seq(Hop(stationA, stationB, train1), Hop(stationB, stationC, train1)))
    }

    "return four connections for the book setup at 12:00" in {
      val ice11 = Train(Ice("11"), List(Time(10) -> stationA, Time(11) -> stationB, Time(12) -> stationD))
      val ice12 = Train(Ice("12"), List(Time(13) -> stationD, Time(14) -> stationB, Time(15) -> stationA))
      val ice13 = Train(Ice("13"), List(Time(16) -> stationA, Time(17) -> stationB, Time(18) -> stationD))
      val ice14 = Train(Ice("14"), List(Time(19) -> stationD, Time(20) -> stationB, Time(21) -> stationA))
      val ice21 = Train(Ice("21"), List(Time(12) -> stationA, Time(13) -> stationC, Time(14) -> stationD))
      val ice22 = Train(Ice("22"), List(Time(15) -> stationD, Time(16) -> stationC, Time(17) -> stationA))
      val ice23 = Train(Ice("23"), List(Time(18) -> stationA, Time(19) -> stationC, Time(20) -> stationD))
      val ice24 = Train(Ice("24"), List(Time(21) -> stationD, Time(22) -> stationC, Time(23) -> stationA))
      val re1 = Train(Re("1"), List(Time(11, 30) -> stationB, Time(12, 30) -> stationC))
      val re2 = Train(Re("2"), List(Time(15, 30) -> stationC, Time(16, 30) -> stationB))
      val journeyPlanner =
        new JourneyPlanner(Set(ice11, ice12, ice13, ice14, ice21, ice22, ice23, ice24, re1, re2))
      val connections = journeyPlanner.connections(stationA, stationD, "12:00")
      connections must haveSize(5)
      connections mustContain List(
          Hop(stationA, stationB, ice13),
          Hop(stationB, stationD, ice13))
      connections mustContain List(
          Hop(stationA, stationC, ice21),
          Hop(stationC, stationD, ice21))
      connections mustContain List(
          Hop(stationA, stationC, ice23),
          Hop(stationC, stationD, ice23))
      connections mustContain List(
          Hop(stationA, stationC, ice21),
          Hop(stationC, stationD, ice23))
      connections mustContain List(
          Hop(stationA, stationC, ice21),
          Hop(stationC, stationB, re2),
          Hop(stationB, stationD, ice13))
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
