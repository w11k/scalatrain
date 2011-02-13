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

import org.scalacheck.Gen._
import org.scalacheck.Prop._
import org.specs.{ ScalaCheck, Specification }

class TimeSpec extends Specification with ScalaCheck {

  // Testing the singleton object

  "Calling fromMinutes" should {

    "throw an IllegalArgumentException for negative minutes" in {
      forAll(choose(Int.MinValue, -1)) { (minutes: Int) =>
        Time fromMinutes minutes must throwA[IllegalArgumentException]
      } must pass
    }

    "return a correctly initialized Time instance for minutes within [0, 24 * 60 - 1)" in {
      forAll(choose(0, 24 * 60 - 1)) { (minutes: Int) =>
        val result = Time fromMinutes minutes
        result.hours mustEqual minutes / 60
        result.minutes mustEqual minutes % 60
      } must pass
    }
  }

  // Testing the class

  "Creating a Time" should {

    "throw an IllegalArgumentException for negative hours" in {
      forAll(choose(Int.MinValue, -1)) { (hours: Int) =>
        Time(hours, 0) must throwA[IllegalArgumentException]
      } must pass
    }

    "throw an IllegalArgumentException for hours >= 24" in {
      forAll(choose(24, Int.MaxValue)) { (hours: Int) =>
        Time(hours, 0) must throwA[IllegalArgumentException]
      } must pass
    }

    "throw an IllegalArgumentException for negative minutes" in {
      forAll(choose(Int.MinValue, -1)) { (minutes: Int) =>
        Time(0, minutes) must throwA[IllegalArgumentException]
      } must pass
    }

    "throw an IllegalArgumentException for minutes >= 60" in {
      forAll(choose(60, Int.MaxValue)) { (minutes: Int) =>
        Time(0, minutes) must throwA[IllegalArgumentException]
      } must pass
    }

    "return an instance with correct defaults" in {
      val time = new Time
      time.hours mustEqual 0
      time.minutes mustEqual 0
    }
  }

  "Calling minus or -" should {
    val time1 = Time(2, 20)
    val time2 = Time(1, 10)

    "throw an IllegalArgumentException for a null that" in {
      time1 minus null must throwA[IllegalArgumentException]
    }

    "return the correct time difference" in {
      time1 - time2 mustEqual 70
      time2 - time1 mustEqual -70
    }
  }

  "Calling asMinutes" should {

    "return the correct value" in {
      Time(0, 10).asMinutes mustEqual 10
      Time(1, 10).asMinutes mustEqual 70
    }
  }

  "Calling toString" should {

    "return a string formatted like hh:mm" in {
      Time().toString mustEqual "00:00"
      Time(1, 1).toString mustEqual "01:01"
      Time(20, 20).toString mustEqual "20:20"
    }
  }

  "Comparing Time instances" should {

    "throw an IllegalArgumentException for a null that" in {
      Time() compare null must throwA[IllegalArgumentException]
    }

    "return expected results when calling the likes of < and >" in {
      Time(0) < Time(1) mustBe true
    }
  }
}
