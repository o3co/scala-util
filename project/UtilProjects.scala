import sbt._
import Keys._

object Utils extends Build 
{
  import BuildSettings._
  import Dependencies._

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
        compile(
          commonsLang
        ) ++
        test(
          specs2
        )
    )
    .dependsOn(counter)
}
