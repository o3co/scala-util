import sbt._
import Keys._

object Utils extends Build 
{
  import BuildSettings._
  import Dependencies._

  lazy val root = Project("util", file("util"))
    .settings(basicSettings: _*)
    .settings(noPublishing: _*)
    .aggregate(
      // 
      Utils.box,
      Utils.counter,
      Utils.generator,
      Utils.rest
    )
    .settings(
      aggregate in update := false
    )
  lazy val counter = Project("util-counter", file("util-counter"))
    .settings(basicSettings: _*)
    .settings(
      libraryDependencies ++= 
        test(
          specs2
        )
    )

  lazy val generator = Project("util-generator", file("util-generator"))
    .settings(basicSettings: _*)
    .settings(
      libraryDependencies ++= 
        compile (
          commonsLang
        ) ++
        test(
          specs2
        )
    )
    .dependsOn(counter)

  lazy val rest = Project("util-rest", file("util-rest"))
    .settings(basicSettings: _*)
    .settings(
      libraryDependencies ++= 
        provided (
          sprayRouting,
          akkaActor
        ) ++ 
        test (
          specs2,
          sprayTestKit,
          json4sNative 
        )

    )

  lazy val box = Project("util-box", file("util-box"))
    .settings(basicSettings: _*)
    .settings(
      libraryDependencies ++= 
        compile (
          scalaReflect
        ) ++
        provided (
          json4sCore
        ) ++ 
        test (
          specs2
        )
    )
}
