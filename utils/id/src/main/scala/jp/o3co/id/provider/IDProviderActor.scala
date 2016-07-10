package jp.o3co.id.provider

import akka.actor.Actor
import akka.pattern.pipe
import scala.concurrent.ExecutionContext

/**
 *
 */
trait IDProviderActorLike[ID] {
  this: Actor with IDProviderLike[ID] =>

  val protocol: IDProviderProtocolLike[ID]
  
  import protocol._

  implicit def executionContext: ExecutionContext
  
  /**
   *
   */
  def receiveIdProviderEvents: Receive = {
    case Exists(id) => 
      existsAsync(id)
        .map(exists => ExistsSuccess(exists))
        .pipeTo(sender())
    case Generate() => 
      generateAsync()
        .map(id => GenerateSuccess(id))
        .pipeTo(sender())
    case Release(id) => 
      releaseAsync(id)
        .map(_ => ReleaseSuccess())
        .pipeTo(sender())
  }
}

trait IDProviderActor[ID] extends Actor with IDProviderActorLike[ID] {
  this: IDProvider[ID] => 

  def receive = receiveIdProviderEvents orElse receiveExtensions

  def receiveExtensions: Receive = Actor.emptyBehavior
}
