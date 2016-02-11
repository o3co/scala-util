package jp.o3co.generator
package impl

import org.specs2.mutable.Specification

/**
 *
 */
class RandomStringGeneratorSpec extends Specification {

  "RandomStringGenerator" should {
    "generate random string" in {
      val generator = RandomStringGenerator(16)

      val generated = generator.generate()
      generated.size === 16
      generated.matches("[A-Za-z0-9]{16}") === true
    } 
    "generate random string with length" in {
      val generator16 = RandomStringGenerator(16)
      generator16.generate().size === 16 

      val generator32 = RandomStringGenerator(32)
      generator32.generate().size === 32
    }
  }
}

