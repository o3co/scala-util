package o3co.actor

import akka.actor.Actor

/**
 *
 */
trait ServiceActorLike {
  this: Actor => 

  def receiveExtension: Receive = Actor.emptyBehavior
}

/**
 *
 */
trait ServiceActor extends Actor with ServiceActorLike
