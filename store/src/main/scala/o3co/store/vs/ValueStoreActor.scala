package o3co.store.vs 

import akka.actor.Actor
import akka.pattern.pipe
import o3co.actor.ServiceActor
import o3co.store
import scala.concurrent.ExecutionContext

trait ReceiveValueStore[V] extends store.ReceiveStore {
  this: Actor =>

  val protocol: ValueStoreProtocol[V] 
}

trait ReceiveReadAccess[V] extends ReceiveValueStore[V] {
  this: Actor with ReadAccess[V] =>

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

trait ReceiveWriteAccess[V] extends ReceiveValueStore[V] {
  this: Actor with WriteAccess[V] =>

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

trait ValueStoreReceiver[V] extends store.ReceiveCountableStore with ReceiveReadAccess[V] with ReceiveWriteAccess[V] {
  this: Actor with ValueStore[V] => 

  override def receiveStoreCommands = 
    super[ReceiveCountableStore].receiveStoreCommands orElse 
    super[ReceiveReadAccess].receiveStoreCommands orElse 
    super[ReceiveWriteAccess].receiveStoreCommands
}

trait ValueStoreActor[V] extends ServiceActor with ValueStoreReceiver[V] {
  this: ValueStore[V] =>

  def receive = receiveStoreCommands orElse receiveExtension
}
