package jp.o3co.generator
package impl

import org.specs2.mutable.Specification

class CountupGeneratorSpec extends Specification {

  import BaseImplicitConversions._ 

  "CountupGenerator" should {
    "generate int value" in {
      val generator = CountupGenerator[Int]()

      1 === generator.generate()
      2 === generator.generate()
      3 === generator.generate()
    }
  }
}
