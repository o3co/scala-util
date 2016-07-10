package jp.o3co.id.domain.provider

import akka.actor.Actor
import akka.pattern.pipe
import jp.o3co.id.provider.IDProviderActorLike
import jp.o3co.id.provider.IDProviderActor

trait DomainIDProviderActorLike[ID] extends IDProviderActorLike[ID] {
  this: Actor with DomainIDProvider[ID] => 

  val protocol: DomainIDProviderProtocolLike[ID]

  import protocol._

  def receiveDomainIdProviderEvents: Receive = {
    case ExistsWithDomain(id, domain) => 
      existsAsync(id, domain)
        .map(exists => ExistsSuccess(exists))
        .pipeTo(sender())
    case GenerateWithDomain(domain) => 
      generateAsync(domain)
        .map(id => GenerateSuccess(id))
        .pipeTo(sender())
    case ReleaseWithDomain(id, domain) => 
      releaseAsync(id, domain)
        .map(_ => ReleaseSuccess())
        .pipeTo(sender())
  }

}

trait DomainIDProviderActor[ID] extends IDProviderActor[ID] with DomainIDProviderActorLike[ID] {
  this: DomainIDProvider[ID] => 

  override def receive = 
    receiveIdProviderEvents orElse
    receiveDomainIdProviderEvents orElse
    receiveExtensions
}
