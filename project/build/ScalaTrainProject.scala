import sbt._
import reaktor.scct.ScctProject

class ScalaTrainProject(info: ProjectInfo) extends DefaultProject(info) with ScctProject {

  // Dependencies
  val slf4jApi = "org.slf4j" % "slf4j-api" % "1.6.1" withSources
  val logback = "ch.qos.logback" % "logback-classic" % "0.9.28"
  val mockito = "org.mockito" % "mockito-all" % "1.8.5" % "test"
  val scalaCheck = "org.scala-tools.testing" %% "scalacheck" % "1.8" % "test"
  val specs = "org.scala-tools.testing" %% "specs" % "1.6.7" % "test" withSources
}
