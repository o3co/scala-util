package jp.o3co.id.store

/**
 *
 */
trait IDStoreProtocolLike[ID] {
  case class IDExists(id: ID)
  case class IDExistsSuccess(exists: Boolean)
  case class IDExistsFailure(cause: Throwable)

  case class PutID(id: ID)
  case class PutIDSuccess()
  case class PutIDFailure(cause: Throwable)

  case class DeleteID(id: ID)
  case class DeleteIDSuccess()
  case class DeleteIDFailure(cause: Throwable)
}

trait IDStoreProtocol[ID] extends IDStoreProtocolLike[ID] 

