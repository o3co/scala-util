import sbt._
import Keys._

object Build extends Build 
{
  import BuildSettings._
  import Dependencies._

  lazy val root = Project("root", file("."))
    .settings(basicSettings: _*)
    .aggregate(
      // 
      Utils.counter,
      Utils.generator
    )
    .settings(
      aggregate in update := false
    )
}

