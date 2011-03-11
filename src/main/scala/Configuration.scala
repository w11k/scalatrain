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

object Configuration extends JourneyPlannerContext {

  override protected val trains: Set[Train] = {
    val stationA = Station("A")
    val stationB = Station("B")
    val stationC = Station("C")
    val stationD = Station("D")

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

    Set(ice11, ice12, ice13, ice14, ice21, ice22, ice23, ice24, re1, re2)
  }
}
