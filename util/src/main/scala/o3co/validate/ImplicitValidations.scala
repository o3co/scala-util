package o3co.validate

/**
 *
 */
trait ImplicitValidations {
  import scala.language.implicitConversions

  implicit def functionValidation[T](f: T => Boolean) = 
    Validation(f)

  implicit def booleanValidation[T](always: Boolean) = 
    Validation(always)
}

object ImplicitValidations extends ImplicitValidations
