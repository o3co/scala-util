package o3co.store

import akka.actor.Actor 
import akka.pattern.pipe

/**
 *
 */
trait StoreActor extends Actor {
  this: Store =>

  implicit def executionContext = context.dispatcher

  val protocol: StoreProtocol
  import protocol._

  def receiveExtensions = Actor.emptyBehavior
  
  def receiveStoreCommands: Receive = {
    case Count =>
      countAsync
        .map(CountSuccess(_))
        .pipeTo(sender())
  }

  def receive: Receive= receiveStoreCommands orElse
    receiveExtensions
}

object StoreActor {
  
  trait Access {
    this: Actor => 
    
    def receiveStoreCommands: Receive 
  }
}
