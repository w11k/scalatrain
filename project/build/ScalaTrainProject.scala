import sbt._
import reaktor.scct.ScctProject

class ScalaTrainProject(info: ProjectInfo) extends DefaultWebProject(info) with ScctProject {

  // Resolvers
  val sts = ScalaToolsSnapshots

  // Dependencies
  val liftWebkit = "net.liftweb" %% "lift-webkit" % "2.2" withSources
  val liftCommon = "net.liftweb" %% "lift-common" % "2.2" withSources // Just for the sources
  val liftUtil = "net.liftweb" %% "lift-util" % "2.2" withSources // Just for the sources
  val scalaz = "org.scalaz" %% "scalaz-core" % "6.0-SNAPSHOT" withSources
  val slf4jApi = "org.slf4j" % "slf4j-api" % "1.6.1" withSources
  val logback = "ch.qos.logback" % "logback-classic" % "0.9.28"
  val mockito = "org.mockito" % "mockito-all" % "1.8.5" % "test"
  val scalaCheck = "org.scala-tools.testing" %% "scalacheck" % "1.8" % "test"
  val specs = "org.scala-tools.testing" %% "specs" % "1.6.7" % "test" withSources
  val jettyWebapp = "org.eclipse.jetty" % "jetty-webapp" % "7.0.2.v20100331" % "test"
}
