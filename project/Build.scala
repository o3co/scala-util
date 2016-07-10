import sbt._
import Keys._

object Build extends Build
{
  import BuildSettings._
  import Dependencies._

  lazy val root = Project("o3co", file("."))
    .settings(basicSettings: _*)
    .settings(
      libraryDependencies ++= 
        provided (
          scalaLogging
        )
    )
    .aggregate(
      id,
      dictionary,
      search,
      store,
      tag,
      //text,
      util
    )
    .dependsOn(
      id,
      dictionary,
      search,
      store,
      tag,
      //text,
      util
    )
    .settings(
      aggregate in update := false
    )

  lazy val id = Project("id", file("id"))
    .settings(basicSettings: _*)
    .settings(
      libraryDependencies ++= 
        provided (
          akkaActor,
          commonsLang,
          //commonsValidator,
          //rediscala,
          //slick,
          typesafeConfig
        ) ++ 
        test (
          akkaActor,
          commonsLang,
          commonsValidator,
          //rediscala,
          //slick,
          specs2,
          typesafeConfig
        )
    )
    .dependsOn(util, store % "provided,test")

  lazy val dictionary = Project("dictionary", file("dictionary"))
    .settings(basicSettings: _*)
    .settings(
      libraryDependencies ++= 
        provided (
          akkaActor,
          slick,
          typesafeConfig
        )
        ++ 
        test (
          akkaActor,
          typesafeConfig,
          slick,
          specs2
        )
    )
    .dependsOn(util, store % "provided,test")

  lazy val search = Project("search", file("search"))
    .settings(basicSettings: _*)
    .settings(
      libraryDependencies ++= 
        provided (
          slick
        ) ++ 
        compile(
          scalaParserCombinator
        )
    )
    .dependsOn(util)

  lazy val store = Project("store", file("store"))
    .settings(basicSettings: _*)
    .settings(
      libraryDependencies ++= 
        provided (
          akkaActor,
          rediscala,
          slick,
          specs2
        ) 
    )
    .dependsOn(util, search % "provided,test")


  lazy val tag = Project("tag", file("tag"))
    .settings(basicSettings: _*)
    .settings(
      libraryDependencies ++= 
        provided(
          akkaActor,
          shapeless,
          slick
        ) ++
        test (
          akkaActor,
          shapeless,
          slick,
          specs2
        )
    )
    .dependsOn(util, dictionary % "provided,test", store % "provided,test")

  lazy val text = Project("text", file("text"))
    .settings(basicSettings: _*)
    .settings(
    )
    .dependsOn(util)

  lazy val util = Project("util", file("util"))
    .settings(basicSettings: _*)
    .settings(
      libraryDependencies ++= 
        compile (
          scalaReflect
        ) ++ 
        provided (
          akkaActor,
          c3p0,
          commonsLang,
          commonsValidator,
          shapeless,
          typesafeConfig
        ) ++ 
        test (
          akkaActor,
          c3p0,
          commonsLang,
          commonsValidator,
          specs2,
          shapeless,
          typesafeConfig
        )
    )
}
 
