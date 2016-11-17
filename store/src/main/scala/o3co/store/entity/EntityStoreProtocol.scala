package o3co.store
package entity

object EntityStoreProtocol {

  trait Read[K, E <: Entity[K]] extends kvs.KeyValueStoreProtocol.Read[K, E] {
    this: StoreProtocol =>
  }

  trait Write[K, E <: Entity[K]] extends vs.ValueStoreProtocol.Write[E] {
    this: StoreProtocol =>

    case class DeleteByIds(ids: Seq[K])
    case object DeleteByIdsSuccess
    case class  DeleteByIdsFailure(cause: Throwable)
  }

  type Full[K, E <: Entity[K]] = Read[K, E] with Write[K, E]
}

trait EntityStoreProtocol[K, E <: Entity[K]] extends StoreProtocol
  with EntityStoreProtocol.Read[K, E]
  with EntityStoreProtocol.Write[K, E]
{

}
