import sbt._
import Keys._

object Samples extends Build 
{
  import BuildSettings._
  import Dependencies._

  lazy val root = Project("samples", file("samples"))
    .settings(basicSettings: _*)
    .settings(noPublishing: _*)
    .aggregate(
      // 
    )
    .settings(
      aggregate in update := false
    )
}
