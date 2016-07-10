package o3co.id.provider

import akka.actor.Actor
import akka.pattern.pipe
import scala.concurrent.ExecutionContext
import o3co.actor.ServiceActor

/**
 *
 */
trait IdProviderReceiver[ID] {
  this: Actor with IdProvider[ID] =>

  val protocol: IdProviderProtocol[ID]
  
  import protocol._

  implicit def executionContext: ExecutionContext
  
  /**
   *
   */
  def receiveIdProviderCommands: Receive = {
    case Exists(id) => 
      existsAsync(id)
        .map(exists => ExistsSuccess(exists))
        .pipeTo(sender())
    case Generate => 
      generateAsync()
        .map(id => GenerateSuccess(id))
        .pipeTo(sender())
    case Reserve(id) => 
      reserveAsync(id)
        .map(_ => ReserveSuccess)
        .pipeTo(sender())
    case Release(id) => 
      releaseAsync(id)
        .map(_ => ReleaseSuccess)
        .pipeTo(sender())
  }
}

trait IdProviderActor[ID] extends ServiceActor with IdProviderReceiver[ID] {
  this: IdProvider[ID] => 

  def receive = receiveIdProviderCommands orElse receiveExtension
}
