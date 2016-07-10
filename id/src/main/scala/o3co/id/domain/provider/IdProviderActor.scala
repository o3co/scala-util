package o3co.id.domain.provider

import akka.actor.Actor
import akka.pattern.pipe
import o3co.id.{provider => base}
import o3co.actor.ServiceActor

/**
 *
 */
trait IdProviderReceiver[ID] extends base.IdProviderReceiver[ID] {
  this: Actor with IdProvider[ID] => 

  val protocol: IdProviderProtocol[ID]
  import protocol._

  def receiveDomainIdProviderCommands: Receive = {
    case ExistsOnDomain(id, domain) => 
      existsAsync(id, domain)
        .map(exists => ExistsSuccess(exists))
        .pipeTo(sender())
    case GenerateOnDomain(domain) => 
      generateAsync(domain)
        .map(id => GenerateSuccess(id))
        .pipeTo(sender())
    case ReserveOnDomain(id, domain) => 
      reserveAsync(id, domain)
        .map(_ => ReserveSuccess)
        .pipeTo(sender())
    case ReleaseOnDomain(id, domain) => 
      releaseAsync(id, domain)
        .map(_ => ReleaseSuccess)
        .pipeTo(sender())
  }

}

trait IdProviderActor[ID] extends ServiceActor with IdProviderReceiver[ID] {
  this: IdProvider[ID] => 

  override def receive = 
    receiveIdProviderCommands orElse
    receiveDomainIdProviderCommands orElse
    receiveExtension
}
