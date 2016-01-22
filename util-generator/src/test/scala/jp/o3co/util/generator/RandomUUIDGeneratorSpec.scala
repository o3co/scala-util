package jp.o3co.util.generator

import org.specs2.mutable.Specification

/**
 *
 */
class RandomUUIDGeneratorSpec extends Specification {

  "RandomUUIDGenerator" should {
    "generate random UUID" in {
      val generator = RandomUUIDGenerator()
      val one = generator.generate
      val two = generator.generate

      one !== two
    }
  }
}


