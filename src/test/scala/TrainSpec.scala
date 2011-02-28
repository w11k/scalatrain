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

class TrainSpec extends Specification {

  "Creating a Train" should {

    "throw an IllegalArgumentException for a null info" in {
      Train(null, Nil) must throwA[IllegalArgumentException]
    }

    "throw an IllegalArgumentException for a null schedule" in {
      Train(Ice("720"), null) must throwA[IllegalArgumentException]
    }

    "throw an IllegalArgumentException for a schedule with less than two stops" in {
      Train(Ice("720"), Nil) must throwA[IllegalArgumentException]
      Train(Ice("720"), List(Time(0, 0) -> Station("station-0"))) must throwA[IllegalArgumentException]
    }
  }

  "Getting stations" should {

    "return the correct stations in correct sequence" in {
      val train = Train(Ice("720"), List(Time(0, 0) -> Station("0"), Time(1, 1) -> Station("1")))
      train.stations mustEqual List(Station("0"), Station("1"))
    }
  }

  "Calling toString" should {

    "return the correct result" in {
      val schedule = List(Time(0, 0) -> Station("0"), Time(1, 1) -> Station("1"))
      Train(Ice("123", true), schedule).toString mustEqual "ICE 123 (WIFI)"
      Train(Ice("123"), schedule).toString mustEqual "ICE 123"
      Train(Re("123"), schedule).toString mustEqual "RE 123"
      Train(Brb("123"), schedule).toString mustEqual "BRB 123"
    }
  }
}

class StationSpec extends Specification {

  // Testing the singleton object

  "A String" should {

    "be implicitly converted into a Station" in {
      def id(station: Station) = station
      id("test") mustEqual Station("test")
    }
  }

  // Testing the class
  "Creating a Station" should {

    "throw an IllegalArgumentException for a null name" in {
      Station(null) must throwA[IllegalArgumentException]
    }
  }
}
