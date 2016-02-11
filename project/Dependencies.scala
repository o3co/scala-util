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
  val awssdk          = "com.amazonaws"       % "aws-java-sdk"     % "1.10.54"
  val commonsCodec    = "commons-codec"       % "commons-codec"    % "1.10"
  val commonsLang     = "org.apache.commons"  % "commons-lang3"    % "3.4"
  val commonsValidator= "commons-validator"   % "commons-validator"% "1.5.0"
  val json4sCore      = "org.json4s"          %% "json4s-core"     % "3.2.11"
  val json4sNative    = "org.json4s"          %% "json4s-native"   % "3.2.11"
  val scalaReflect    = "org.scala-lang"      %  "scala-reflect"   % BuildSettings.ScalaVersion
  val scalaParserCombinator = "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4"
  val specs2          = "org.specs2"          %% "specs2-core"     % "2.4.17"
  val sprayRouting    = "io.spray"            %% "spray-routing"   % "1.3.3"
  val sprayTestKit    = "io.spray"            %% "spray-testkit"   % "1.3.3"
  val typesafeConfig  = "com.typesafe"        %  "config"          % "1.2.1"
  val scalaLogging    = "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0"
  val slick           = "com.typesafe.slick"  %% "slick"           % "3.1.0"
  val c3p0            = "com.mchange"         %  "c3p0"            % "0.9.5"
  val shapeless       = "com.chuusai"         %% "shapeless"       % "1.2.4"
}

