package jp.o3co.generator

import scala.language.implicitConversions

trait BaseImplicitConversions {

  implicit def functionToValidator[T](f: Function[T, Boolean]): Validator[T] = Validator[T](f)

  implicit def booleanToValidator[T](always: Boolean): Validator[T] = Validator[T](always)

}
object BaseImplicitConversions extends BaseImplicitConversions
