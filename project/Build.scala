import sbt._
import Keys._

object Build extends Build 
{
  import BuildSettings._
  import Dependencies._

  lazy val root = Project("root", file("."))
    .settings(basicSettings: _*)
    .settings(noPublishing: _*)
    .aggregate(
      Samples.root,
      Utils.root
    )
    .settings(
      aggregate in update := false
    )
}

