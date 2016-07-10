import sbt._
import Keys._

/**
 *
 */
object BuildSettings {
  val VERSION      = "0.2-SNAPSHOT"
  val ScalaVersion = "2.11.7"

  import Dependencies._

  val basicSettings = Seq(
      organization  := "o3co",
      scalaVersion  := ScalaVersion,
      version       := VERSION,
      scalacOptions := Seq("-unchecked", "-deprecation", "-feature", "-encoding", "utf8"),
      resolvers     ++= Dependencies.resolutionRepos,
      //sbtPlugin     := true,
      parallelExecution in Test := false,
      //javaOptions in Test += "-Dorg.slf4j.simpleLogger.defaultLogLevel=OFF",
      updateOptions in Global := updateOptions.in(Global).value.withCachedResolution(true),
      scalacOptions in (Compile,doc) := Seq("-groups", "-implicits")
    )

  lazy val noPublishing = seq(
    publish := (),
    publishLocal := (),
    publishTo := None
  )
}


