package o3co.util.generator

import org.specs2.mutable.Specification

class CountupGeneratorSpec extends Specification {

  "CountupGenerator" should {
    "generate int value" in {
      val generator = CountupGenerator[Int]()

      1 === generator.generate 
      2 === generator.generate 
    }

    "re-generate value with Validation" in {
      val generator = CountupGenerator[Int]({ num: Int => num != 2}, 3)

      1 === generator.generate
      3 === generator.generate
    }

    "throw GenerateException when cannot generate" in {
      val generator = CountupGenerator[Int]({ num: Int => false }, 3)

      generator.generate must throwA[GenerateException]
    }
  }
}
