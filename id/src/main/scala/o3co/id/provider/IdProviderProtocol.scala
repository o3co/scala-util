package o3co.id.provider

/**
 *
 */
trait IdProviderProtocol[ID] {
  
  case class Exists(id: ID)
  case class ExistsSuccess(exists: Boolean)
  case class ExistsFailure(cause: Throwable)

  case object Generate
  case class  GenerateSuccess(id: ID)
  case class  GenerateFailure(cause: Throwable)

  case class  Reserve(id: ID)
  case object ReserveSuccess
  case class  ReserveFailure(cause: Throwable)

  case class  Release(id: ID)
  case object ReleaseSuccess
  case class  ReleaseFailure(cause: Throwable)
}

