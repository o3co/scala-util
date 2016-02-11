package jp.o3co.generator

class GeneratorException(message: String = null, cause: Throwable = null) extends RuntimeException(message, cause)
object GeneratorException {
  def apply()                                  = new GeneratorException()
  def apply(message: String)                   = new GeneratorException(message, null)
  def apply(cause: Throwable)                  = new GeneratorException(null, cause)
  def apply(message: String, cause: Throwable) = new GeneratorException(message, cause)
}

case class ValidationException(message: String = null, cause: Throwable = null) extends GeneratorException(message, cause)
