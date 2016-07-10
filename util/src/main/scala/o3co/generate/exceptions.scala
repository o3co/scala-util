package o3co.generate

case class GenerateException(message: String = null, cause: Throwable = null) extends RuntimeException(message, cause)

