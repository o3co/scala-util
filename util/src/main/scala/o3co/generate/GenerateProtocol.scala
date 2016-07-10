package o3co.generate

/**
 */
trait GenerateProtocolLike[T] {
  
  case object Generate
  case class  GenerateSuccess(value: T)
  case class  GenerateFailure(cause: Throwable)
}

trait GenerateProtocol[T] extends GenerateProtocolLike[T]

