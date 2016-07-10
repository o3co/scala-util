package o3co.store

import akka.actor.Actor 
import akka.pattern.pipe
import o3co.actor.ServiceActor
import scala.concurrent.ExecutionContext

trait ReceiveStore {
  this: Actor => 

  implicit def executionContext: ExecutionContext

  val protocol: StoreProtocol
  import protocol._

  def receiveStoreCommands: Receive 
  //= {
  //  case Count => 
  //    countAsync
  //      .map(CountSuccess(_))
  //      .pipeTo(sender())
  //}
}

trait ReceiveCountableStore extends ReceiveStore {
  this: Actor with Store => 

  val protocol: StoreProtocol
  import protocol._

  def receiveStoreCommands = {
    case Count => 
      countAsync
        .map(CountSuccess(_))
        .pipeTo(sender())
  }
}

trait StoreActorLike extends ReceiveStore {
  this: Actor with Store => 
}

trait StoreActor extends ServiceActor with StoreActorLike {
  this: Store => 
  def receive = receiveStoreCommands orElse receiveExtension
}

