package jp.o3co.routing

case class DelegateException(message: String = null, cause: Throwable = null) extends RuntimeException(message, cause)
