import sbt._
import reaktor.scct.ScctProject

class ScalaTrainProject(info: ProjectInfo) extends DefaultProject(info) with ScctProject {

  // Dependencies
  val mockito = "org.mockito" % "mockito-all" % "1.8.5" % "test"
  val scalaCheck = "org.scala-tools.testing" %% "scalacheck" % "1.8" % "test"
  val specs = "org.scala-tools.testing" %% "specs" % "1.6.7" % "test" withSources
}
