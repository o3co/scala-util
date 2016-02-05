import sbt._

object Dependencies {
  import BuildSettings._

  val resolutionRepos = Seq(
  )

  // 
  def compile   (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "compile")
  def provided  (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "provided")
  def test      (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "test")
  def runtime   (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "runtime")
  def container (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "container")

  // Dependencies
  val akkaActor       = "com.typesafe.akka"   %% "akka-actor"      % "2.3.14"
  val commonsLang     = "org.apache.commons"     % "commons-lang3"    % "3.4"
  val json4sCore      = "org.json4s"          %% "json4s-core"     % "3.2.11"
  val json4sNative    = "org.json4s"          %% "json4s-native"     % "3.2.11"
  val scalaReflect    = "org.scala-lang"      %  "scala-reflect"   % BuildSettings.ScalaVersion
  val specs2          = "org.specs2"             %% "specs2-core"     % "2.4.17"
  val sprayRouting    = "io.spray"            %% "spray-routing"   % "1.3.3"
  val sprayTestKit    = "io.spray"            %% "spray-testkit"   % "1.3.3"
  val typesafeConfig  = "com.typesafe"        %  "config"          % "1.2.1"
}

