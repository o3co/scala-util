import sbt._
import Keys._


object BuildSettings {
  val VERSION      = "0.1-SNAPSHOT"
  val ScalaVersion = "2.11.7"

  import Dependencies._

  val basicSettings = Seq(
      organization  := "jp.o3co",
      scalaVersion  := ScalaVersion,
      version       := VERSION,
      scalacOptions := Seq("-unchecked", "-deprecation", "-feature", "-encoding", "utf8"),
      resolvers     ++= Dependencies.resolutionRepos,
      //sbtPlugin     := true,
      parallelExecution in Test := false,
      //javaOptions in Test += "-Dorg.slf4j.simpleLogger.defaultLogLevel=OFF",
      updateOptions in Global := updateOptions.in(Global).value.withCachedResolution(true)
    )

  //lazy val moduleSettings = seq(
  //  crossPaths := false,
  //  publishMavenStyle := true,
  //  SbtPgp.useGpg := true,
  //  publishTo <<= version { version =>
  //    Some {
  //      "jp.o3co nexus" at {
  //        "http://localhost:42424/repositories/" + {
  //          if (version.trim.endsWith("SNAPSHOT")) "snapshots"
  //          else "releases/"
  //        }
  //      }
  //    }
  //  },
  //  pomIncludeRepository := { _ => false },
  //  pomExtra := 
  //    <scm>
  //      <url>git://github.com/o3co/scala-util.git</url>
  //      <connection>scm:git@github.com:o3co/scala-util.git</connection>
  //    </scm>
  //    <developers>
  //      <developer><id>yoshi</id><name>Yoshi Aoki</name></developer>
  //    </developers>
  //)

  lazy val noPublishing = seq(
    publish := (),
    publishLocal := (),
    publishTo := None
  )
}

