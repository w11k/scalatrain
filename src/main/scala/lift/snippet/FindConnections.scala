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
package lift
package snippet

import Configuration.journeyPlanner
import net.liftweb.http.{ S, SHtml, StatefulSnippet }
import net.liftweb.util.BindHelpers._
import scala.collection.immutable.Seq
import scalaz.Scalaz._
import scalaz.{ Failure, NonEmptyList, Success, Validation }

class FindConnections extends StatefulSnippet {

  override def dispatch = {
    case _ => render
  }

  private var from = ""

  private var to = ""

  private var departureTime = ""

  private var connections: Set[Seq[Hop]] = Set.empty

  private def render = {
    def handleSubmit() {
      val from: Validation[NonEmptyList[String], Station] =
        if (!(journeyPlanner.stations contains Station(this.from)))
          "Unknown from-station: %s" format this.from failNel
        else
          Station(this.from).success
      val to: Validation[NonEmptyList[String], Station] =
        if (!(journeyPlanner.stations contains Station(this.to)))
          "Unknown to-station: %s" format this.to failNel
        else
          Station(this.to).success
      val departureTime: Validation[NonEmptyList[String], Time] =
        try {
          (Time stringToTime this.departureTime).success
        } catch {
          case e: IllegalArgumentException => "Illegal format for departureTime: %s" format this.departureTime failNel
        }
      (from |@| to |@| departureTime) { journeyPlanner.connections } match {
        case Success(connections) =>
          this.connections = connections
        case Failure(errors) =>
          errors foreach S.error
      }
    }

    def renderConnection(connection: Seq[Hop]) =
      ".from *" #> connection.head.from.name &
      ".departureTime *" #> connection.head.departureTime.toString &
      ".to *" #> connection.last.to.name &
      ".arrivalTime *" #> connection.last.arrivalTime.toString &
      ".trains *" #> (connection map { _.train } mkString ", ")

    import SHtml._
    "#from" #> text(from, from = _) &
    "#to" #> text(to, to = _) &
    "#departureTime" #> text(departureTime, departureTime = _) &
    ".connection" #> (connections map renderConnection) &
    "#findConnections" #> onSubmitUnit{ handleSubmit }
  }
}
