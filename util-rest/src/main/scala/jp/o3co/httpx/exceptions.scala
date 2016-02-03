package jp.o3co.httpx

case class DelegateException(message: String = null, cause: Throwable = null) extends RuntimeException(message, cause)
