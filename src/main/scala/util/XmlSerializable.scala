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
package util

import scala.xml.NodeSeq

trait XmlSerializable[A] {

  implicit def fromXml(xml: NodeSeq)(implicit format: XmlFormat[A]): A = {
    require(xml != null, "xml must not be null!")
    require(format != null, "format must not be null!")
    format fromXml xml
  }

  implicit def toToXml(a: A): ToXml[A] = {
    require(a != null, "a must not be null!")
    new ToXml(a)
  }
}

private[util] class ToXml[A](a: A) {

  def toXml(implicit format: XmlFormat[A]): NodeSeq =
    format toXml a
}

trait XmlFormat[A] {

  def toXml(a: A): NodeSeq

  def fromXml(xml: NodeSeq): A
}
