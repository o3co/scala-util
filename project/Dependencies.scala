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
  val commonsLang     = "org.apache.commons"     % "commons-lang3"    % "3.4"
  val specs2          = "org.specs2"             %% "specs2-core"     % "2.4.17"
}

