package com.typesafe.config

import org.specs2.mutable.Specification

class ScalaLikeOperationsSpec extends Specification {

  "ScalaLikeOperations" should {
    "ok" in {
      val conf = new ScalaLikeOperations{
        val underlying = 
          ConfigFactory.parseString("""
foo: {
  name: foo
  numeric: 1
}
bar : bar
""" 
          )
      }
        .toMap
      
      conf must haveKeys("foo", "bar")
      conf must have size(2) 
    }
  }
}
