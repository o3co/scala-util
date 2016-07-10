package jp.o3co.id.provider

/**
 *
 */
trait IDProviderProtocolLike[ID] {
  
  case class Exists(id: ID)
  case class ExistsSuccess(exists: Boolean)
  case class ExistsFailure(cause: Throwable)

  case class Generate()
  case class GenerateSuccess(id: ID)
  case class GenerateFailure(cause: Throwable)

  case class Release(id: ID)
  case class ReleaseSuccess()
  case class ReleaseFailure(cause: Throwable)
}

trait IDProviderProtocol[ID] extends IDProviderProtocolLike[ID]
