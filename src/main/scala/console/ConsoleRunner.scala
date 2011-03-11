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
package console

import Configuration.journeyPlanner
import scala.collection.immutable.Seq
import scalaz.Scalaz._
import scalaz.{ Failure, NonEmptyList, Success, Validation }

object ConsoleRunner {

  def main(args: Array[String]) {
    (from(args) |@| to(args) |@| departureTime(args)) { journeyPlanner.connections } match {
      case Success(connections) =>
        connections foreach { connection => println(formatConnection(connection)) }
      case Failure(errors) =>
        println("Please correct the following errors:")
        errors foreach println
    }
  }

  private def from(args: Array[String]): Validation[NonEmptyList[String], Station] =
    if (args.length < 1)
      "Missing argument for from-station!".failNel
    else if (!(journeyPlanner.stations contains Station(args(0))))
      "Unknown from-station: %s" format args(0) failNel
    else
      Station(args(0)).success

  private def to(args: Array[String]): Validation[NonEmptyList[String], Station] =
    if (args.length < 2)
      "Missing argument for to-station!".failNel
    else if (!(journeyPlanner.stations contains Station(args(1))))
      "Unknown to-station: %s" format args(1) failNel
    else
      Station(args(1)).success

  private def departureTime(args: Array[String]): Validation[NonEmptyList[String], Time] =
    if (args.length < 3)
      "Missing argument for departureTime!".failNel
    else
      try {
        (Time stringToTime args(2)).success
      } catch {
        case e: IllegalArgumentException => "Illegal format for departureTime: %s" format args(2) failNel
      }

  private def formatConnection(connection: Seq[Hop]) = {
    "Departure from %s at %s, arrival at %s at %s. Using these trains: %s.".format(
        connection.head.from.name,
        connection.head.departureTime,
        connection.last.to.name,
        connection.last.arrivalTime,
        connection map { _.train } mkString ", ")
  }
}
