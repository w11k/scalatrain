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
import scala.collection.immutable.Seq

class utilSpec extends Specification {
  import util._

  "Calling isIncreasing" should {

    "throw an IllegalArgumentException for a null xs" in {
      isIncreasing(null) must throwA[IllegalArgumentException]
    }

    "return true for a empty sequence" in {
      isIncreasing(Nil) mustBe true
    }

    "return true for a sequence with one element" in {
      isIncreasing(1 :: Nil) mustBe true
    }

    "return true for a increasing sequence" in {
      isIncreasing(1 :: 2 :: 4 :: 99 :: Nil) mustBe true
    }

    "return false for a not-increasing sequence" in {
      isIncreasing(1 :: 1 :: Nil) mustBe false
      isIncreasing(1 :: 2 :: 1 :: Nil) mustBe false
    }
  }
}
