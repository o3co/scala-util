package jp.o3co.generator

trait GeneratorProtocolLike[T] {
  
  case object Generate

  case class GenerateComplete(value: T)

  case class GenerateFailure(cause: Throwable)
}
