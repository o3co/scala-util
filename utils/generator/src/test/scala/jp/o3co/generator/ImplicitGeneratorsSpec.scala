package jp.o3co.generator

import java.util.UUID
import org.specs2.mutable.Specification
import jp.o3co.generator.impl._

class ImplicitGeneratorsSpec extends Specification with ImplicitGenerators {

  "ImplicitGenerators" should {
    "resolve Generator[Int]" in {
      val gen = implicitly[Generator[Int]]
      gen must beAnInstanceOf[CountupGenerator[Int]]
    }
    "resolve Generator[Double]" in {
      val gen = implicitly[Generator[Double]]
      gen must beAnInstanceOf[CountupGenerator[Double]]
    }
    "resolve Generator[Float]" in {
      val gen = implicitly[Generator[Float]]
      gen must beAnInstanceOf[CountupGenerator[Float]]
    }
    "resolve Generator[Long]" in {
      val gen = implicitly[Generator[Long]]
      gen must beAnInstanceOf[CountupGenerator[Long]]
    }
    "resolve Generator[Short]" in {
      val gen = implicitly[Generator[Short]]
      gen must beAnInstanceOf[CountupGenerator[Short]]
    }
    "resolve Generator[String]" in {
      val gen = implicitly[Generator[String]]
      gen must beAnInstanceOf[RandomStringGenerator]
    }
    "resolve Generator[UUID]" in {
      val gen = implicitly[Generator[UUID]]
      gen must beAnInstanceOf[RandomUUIDGenerator]
    }
  }
}
