package o3co.store

/**
 * Foundation of store protocol
 */
trait StoreProtocol {
  case object Count
  case class  CountSuccess(count: Long)
  case class  CountFailure(cause: Throwable)
}

