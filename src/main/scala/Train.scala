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

import scala.collection.immutable.Seq

case class Train(kind: String, number: String, schedule: Seq[(Time, Station)]) {
  require(kind != null, "kind must not be null!")
  require(number != null, "number must not be null!")
  require(schedule != null, "schedule must not be null!")
  require(schedule.size >= 2, "schedule must have at least two stops!")

  val stations: Seq[Station] =
    schedule map { _._2 }
}

case class Station(name: String) {
  require(name != null, "name must not be null!")
}
