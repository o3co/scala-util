package o3co.store
package vs 

import akka.actor.Actor
import akka.pattern.pipe
import o3co.actor.ServiceActor
import o3co.store
import scala.concurrent.ExecutionContext

object ValueStoreActor {

  trait Read[V] extends StoreActor.Access {
    this: Actor with Store with ValueStore.Read[V] =>

    val protocol: ValueStoreProtocol.Read[V]
    import protocol._

    def receiveStoreCommands = {
      case ValueExists(value) =>
        existsAsync(value)
          .map(ValueExistsSuccess(_))
          .pipeTo(sender())
      case GetValues =>
        valuesAsync()
          .map(GetValuesSuccess(_))
          .pipeTo(sender())
    }
  }

  trait Write[V] extends StoreActor.Access {
    this: Actor with Store with ValueStore.Write[V] =>

    val protocol: ValueStoreProtocol.Write[V]
    import protocol._

    def receiveStoreCommands = {
      case AddValues(values) =>
        addAsync(values: _*)
          .map(_ => AddValuesSuccess)
          .pipeTo(sender())
      case PutValues(values) =>
        putAsync(values: _*)
          .map(_ => PutValuesSuccess)
          .pipeTo(sender())
      case DeleteValues(values) =>
        deleteAsync(values: _*)
          .map(_ => DeleteValuesSuccess)
          .pipeTo(sender())
    }
  }

  type Full[V] = Read[V] with Write[V] 
}

/**
 *
 */
trait ValueStoreActor[V] extends StoreActor with ValueStoreActor.Read[V] with ValueStoreActor.Write[V] {
  this: Store with ValueStore.Read[V] with ValueStore.Write[V] =>

  val protocol: StoreProtocol with ValueStoreProtocol.Full[V]

  override def receiveStoreCommands = {
    super[StoreActor].receiveStoreCommands orElse 
    super[Read].receiveStoreCommands orElse 
    super[Write].receiveStoreCommands
  }
}
