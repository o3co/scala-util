package jp.o3co.generator

import java.util.UUID

trait ImplicitGenerators extends BaseImplicitConversions {

  val defaultRandomStringLen  = 16
  val defaultRandomStringType = impl.RandomStringGenerator.ValueTypes.Any

  implicit lazy val intGenerator: Generator[Int]       = impl.CountupGenerator[Int]()
  implicit lazy val doubleGenerator: Generator[Double] = impl.CountupGenerator[Double]()
  implicit lazy val floatGenerator: Generator[Float]   = impl.CountupGenerator[Float]()
  implicit lazy val longGenerator: Generator[Long]     = impl.CountupGenerator[Long]()
  implicit lazy val shortGenerator: Generator[Short]   = impl.CountupGenerator[Short]()
  implicit lazy val stringGenerator: Generator[String] = impl.RandomStringGenerator(defaultRandomStringLen, defaultRandomStringType)
  implicit lazy val uuidGenerator: Generator[UUID]     = impl.RandomUUIDGenerator()
}
object ImplicitGenerators extends ImplicitGenerators
