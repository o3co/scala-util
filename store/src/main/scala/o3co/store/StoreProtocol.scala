package o3co.store


trait StoreProtocol {
  case object Count
  case class CountSuccess(total: Long)
  case class CountFailure(cause: Throwable)
}
