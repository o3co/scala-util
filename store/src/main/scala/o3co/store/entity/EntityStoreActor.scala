package o3co.store
package entity


object EntityStoreActor {
  
  type Read[K, E <: Entity[K]] = kvs.KeyValueStoreActor.Read[K, E]

  type Write[K, E <: Entity[K]] = vs.ValueStoreActor.Write[E]

  type Full[K, E <: Entity[K]] = Read[K, E] with Write[K, E]
}

trait EntityStoreActor[K, E <: Entity[K]] extends StoreActor 
  with EntityStoreActor.Read[K, E]
  with EntityStoreActor.Write[K, E]
{
  this: Store with EntityStore.Full[K, E] =>

  override val protocol: StoreProtocol with EntityStoreProtocol.Full[K, E]

  override def receiveStoreCommands = 
    super[StoreActor].receiveStoreCommands orElse 
    super[Read].receiveStoreCommands orElse 
    super[Write].receiveStoreCommands
}
