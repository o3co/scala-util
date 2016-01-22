package jp.o3co.httpx.request.delegator

case class DelegateException(msg: String = null, cause: Throwable = null) extends RuntimeException(msg, cause)
