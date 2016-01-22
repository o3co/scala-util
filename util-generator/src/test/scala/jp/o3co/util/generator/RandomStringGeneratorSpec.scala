package jp.o3co.util.generator

import org.specs2.mutable.Specification

/**
 *
 */
class RandomStringGeneratorSpec extends Specification {

  "RandomStringGenerator" should {
    "generate random string" in {
      val generator = RandomStringGenerator(16, {value: String => true}, 3)
      val one = generator.generate
      val two = generator.generate

      one !== two
    }
    "generate random string with length" in {
      val generator16 = RandomStringGenerator(16)
      generator16.generate.size === 16 

      val generator32 = RandomStringGenerator(32)
      generator32.generate.size === 32
    }
  }
}

