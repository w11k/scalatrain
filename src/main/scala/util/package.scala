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

import scala.annotation.tailrec
import scala.collection.immutable.Seq

package object util {

  def hasNoDuplicates(as: Seq[_]): Boolean = {
    require(as != null, "as must not be null!")
    as == as.distinct
  }

  @tailrec def isIncreasing[A <% Ordered[A]](as: Seq[A]): Boolean = {
    require(as != null, "as must not be null!")
    as match {
      case a1 :: a2 :: as => (a1 < a2) && isIncreasing(a2 +: as)
      case _ => true
    }
  }
}
