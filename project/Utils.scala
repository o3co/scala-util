import sbt._
import Keys._

object Utils extends Build 
{
  import BuildSettings._
  import Dependencies._

  lazy val root = Project("utils", file("utils"))
    .settings(basicSettings: _*)
    .settings(
      libraryDependencies ++= 
        provided (
          scalaLogging
        )
    )
    .aggregate(
      aws,
      box,
      collection,
      config,
      converter,
      counter,
      datastore,
      dictionary,
      generator,
      id,
      matcher,
      misc,
      routeDelegator,
      search,
      tag,
      tagDictionary
    )
    .dependsOn(
      aws,
      box,
      collection,
      config,
      converter,
      counter,
      datastore,
      dictionary,
      generator,
      id,
      matcher,
      misc,
      routeDelegator,
      search,
      tag,
      tagDictionary
    )
    .settings(
      aggregate in update := false
    )

  lazy val aws = Project("util-aws", file("utils/aws"))
    .settings(basicSettings: _*)
    .settings(
      libraryDependencies ++= 
        provided (
          awssdk,
          typesafeConfig
        ) ++
        test (
          awssdk,
          specs2
        )
    )
    .dependsOn(config)

  lazy val box = Project("util-box", file("utils/box"))
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

  lazy val collection = Project("util-collection", file("utils/collection"))
    .settings(basicSettings: _*)
    .settings(
      libraryDependencies ++= 
        compile (
          scalaReflect
        ) ++
        test (
          specs2
        )
    )

  lazy val config = Project("util-config", file("utils/config"))
    .settings(basicSettings: _*)
    .settings(
      libraryDependencies ++= 
        compile (
          scalaReflect
        ) ++
        provided (
          akkaActor,
          c3p0,
          scalaLogging,
          typesafeConfig
        ) ++
        test(
          commonsLang,
          specs2
        )
    )
    .dependsOn(
      misc
    )

  lazy val converter = Project("util-converter", file("utils/converter"))
    .settings(basicSettings: _*)
    .settings(
      libraryDependencies ++= 
        provided (
          commonsCodec
        ) ++
        test (
          commonsCodec,
          specs2
        )
    )

  lazy val counter = Project("util-counter", file("utils/counter"))
    .settings(basicSettings: _*)
    .settings(
      libraryDependencies ++= 
        test(
          specs2
        )
    )

  lazy val datastore = Project("util-datastore", file("utils/datastore"))
    .settings(basicSettings: _*)
    .settings(
      libraryDependencies ++= 
        provided (
          akkaActor
        )
    )
    .dependsOn(
      tag % "provided,test", 
      search % "provided,test"
    )

  lazy val dictionary = Project("util-dictionary", file("utils/dictionary"))
    .settings(basicSettings: _*)
    .settings(
      libraryDependencies ++= 
        provided (
          akkaActor,
          typesafeConfig
        )
        ++ 
        test (
          specs2
        )
    )
    .dependsOn(
      config % "provided,test,runtime"  
    )

  lazy val generator = Project("util-generator", file("utils/generator"))
    .settings(basicSettings: _*)
    .settings(
      libraryDependencies ++= 
        compile (
          commonsLang
        ) ++
        provided (
          akkaActor
        ) ++
        test(
          specs2,
          commonsLang
        )
    )
    .dependsOn(counter)

  lazy val id = Project("util-id", file("utils/id"))
    .settings(basicSettings: _*)
    .settings(
      libraryDependencies ++= 
        provided (
          akkaActor,
          slick,
          rediscala
        ) ++
        test(
          specs2,
          commonsLang
        )
    )
    .dependsOn(config % "provided,test", generator % "provided,test")

  lazy val matcher = Project("util-matcher", file("utils/matcher"))
    .settings(basicSettings: _*)
    .settings(
      libraryDependencies ++=
        compile(
          shapeless
        ) ++ 
        provided (
          commonsLang
        ) ++
        test(
          specs2,
          commonsLang
        )
    )

  lazy val misc = Project("util-misc", file("utils/misc"))
    .settings(basicSettings: _*)
    .settings(
      libraryDependencies ++=
        provided (
          commonsCodec,
          commonsLang,
          commonsValidator
        ) ++ 
        test (
          specs2
        )
    )

  lazy val routeDelegator = Project("util-route-delegator", file("utils/route-delegator"))
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

  lazy val search = Project("util-search", file("utils/search"))
    .settings(basicSettings: _*)
    .settings(
      libraryDependencies ++= 
        provided(
          scalaParserCombinator,
          slick
        ) ++ 
        test (
          specs2,
          sprayTestKit,
          json4sNative,
          slick
        )
    )
    .dependsOn(
      aws % "provided,test",
      matcher % "provided",
      misc
    )
    
  lazy val tag = Project("util-tag", file("utils/tag"))
    .settings(basicSettings: _*)
    .settings(
      libraryDependencies ++= 
        provided (
          akkaActor,
          slick
        )
    )
    .dependsOn(
      matcher
    )
    
  lazy val tagDictionary = Project("util-tag-ditionary", file("utils/tag-dictionary"))
    .settings(basicSettings: _*)
    .settings(
      libraryDependencies ++= 
        provided (
          akkaActor,
          typesafeConfig,
          slick
        )
    )
    .dependsOn(
      config,
      dictionary,
      generator,
      tag
    )
}
