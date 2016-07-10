package o3co.generate

import java.util.UUID

trait ImplicitGenerates {

  val stringLength    = 16

  implicit lazy val intGenerate: Generate[Int]       = CountupGenerate[Int]()
  implicit lazy val doubleGenerate: Generate[Double] = CountupGenerate[Double]()
  implicit lazy val floatGenerate: Generate[Float]   = CountupGenerate[Float]()
  implicit lazy val longGenerate: Generate[Long]     = CountupGenerate[Long]()
  implicit lazy val shortGenerate: Generate[Short]   = CountupGenerate[Short]()
  implicit lazy val stringGenerate: Generate[String] = RandomStringGenerate(stringLength, RandomStringGenerate.ValueTypes.Any)
  implicit lazy val uuidGenerate: Generate[UUID]     = RandomUUIDGenerate()
}
object ImplicitGenerates extends ImplicitGenerates
