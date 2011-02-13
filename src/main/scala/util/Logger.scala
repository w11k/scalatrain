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

import org.slf4j.{ Logger => SLF4JLogger, LoggerFactory }

trait Logger {

  def error(message: => String) {
    if (slf4jLogger.isErrorEnabled) slf4jLogger.error(message)
  }

  def error(message: => String, t: Throwable) {
    if (slf4jLogger.isErrorEnabled) slf4jLogger.error(message, t)
  }

  def warn(message: => String) {
    if (slf4jLogger.isErrorEnabled) slf4jLogger.warn(message)
  }

  def warn(message: => String, t: Throwable) {
    if (slf4jLogger.isErrorEnabled) slf4jLogger.warn(message, t)
  }

  def info(message: => String) {
    if (slf4jLogger.isInfoEnabled) slf4jLogger.info(message)
  }

  def info(message: => String, t: Throwable) {
    if (slf4jLogger.isInfoEnabled) slf4jLogger.info(message, t)
  }

  def debug(message: => String) {
    if (slf4jLogger.isInfoEnabled) slf4jLogger.debug(message)
  }

  def debug(message: => String, t: Throwable) {
    if (slf4jLogger.isInfoEnabled) slf4jLogger.debug(message, t)
  }

  def trace(message: => String) {
    if (slf4jLogger.isInfoEnabled) slf4jLogger.trace(message)
  }

  def trace(message: => String, t: Throwable) {
    if (slf4jLogger.isInfoEnabled) slf4jLogger.trace(message, t)
  }

  protected val slf4jLogger: SLF4JLogger
}

trait Logging {
  self =>

  protected lazy val logger = new Logger {
    override protected val slf4jLogger = LoggerFactory getLogger self.getClass.getName
  }
}
