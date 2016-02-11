package jp.o3co.collection.mutable

import org.specs2.mutable.Specification

class BiMapSpec extends Specification {

  "BiMap" should {
    "be applied" in {
      val b1 = BiMap()
      b1 must beAnInstanceOf[BiMap[Nothing, Nothing]] 

      val b2 = BiMap("a" -> 123)
      b2 must beAnInstanceOf[BiMap[String, Int]] 

      val b3 = BiMap.empty
      b3 must beAnInstanceOf[BiMap[Nothing, Nothing]] 
    }
    "be mapLike" in {
      val b2 = BiMap("a" -> "A", "b" -> "B")
      b2("a") === "A"
      b2.get("b") === Some("B")
      
      b2.size === 2

      b2("a") = "B"
      b2.size === 1

      b2.contains("a") === true
      b2.contains("b") === false

      b2 must beAnInstanceOf[BiMap[String, String]]
    }

    "support extension methods" in {
      val b = BiMap("a" -> "A", "b" -> "B")

      b.getKey("A") === Some("a")
      b.getKey("a") === None

      b.getKeyOrElse("C", "c") === "c"

      b.size === 2
      b.getKeyOrElseUpdate("D", "d") === "d"
      b.size === 3
    }
  }
}

