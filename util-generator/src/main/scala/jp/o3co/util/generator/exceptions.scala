package jp.o3co.util.generator

case class GenerateException(message: String = null, cause: Throwable = null) extends RuntimeException(message, cause)

