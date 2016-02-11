package jp.o3co.generator

import java.util.UUID
import org.specs2.mutable.Specification
import jp.o3co.generator.impl._

class ValidateGeneratorSpec extends Specification {

  "ValidateGenerator" should {
    "throw an exception when validator return false" in {
      val generator = ValidateGenerator(impl.StaticGenerator(0), {value: Int => value != 0})

      generator.generate() must throwA[GeneratorException]
    }
  }
}

