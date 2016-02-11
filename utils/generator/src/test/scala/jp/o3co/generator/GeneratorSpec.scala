package jp.o3co.generator

import java.util.UUID
import org.specs2.mutable.Specification
import jp.o3co.generator.impl._

trait GeneratorSpecLike extends Specification {

  "Generator" should {
    "create ValidateGenerator withValidation method" in {
      val generator = StaticGenerator(0).withValidation(value => value != 0)

      generator must beAnInstanceOf[ValidateGenerator[Int]]

      generator.generate() must throwA[GeneratorException]
    }
  }
}


