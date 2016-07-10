package jp.o3co.id.domain.provider

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import jp.o3co.id.provider._
import jp.o3co.net.DomainName
import scala.concurrent.ExecutionContext


/**
 *
 */
trait DomainIDProviderActorRefAdapterLike[ID] extends IDProviderActorRefAdapterLike[ID] with DomainIDProviderAdapterLike[ID]{
  
  /**
   *
   */
  val protocol: IDProviderProtocolLike[ID] with DomainIDProviderProtocolLike[ID]
  import protocol._

  /**
   *
   */
  def endpoint: ActorRef

  /**
   *
   */
  implicit def timeout: Timeout

  /**
   *
   */
  implicit def executionContext: ExecutionContext

  /*
   */
  protected def doExistsAsync(id: ID, domain: DomainName) = {
    (endpoint ? ExistsWithDomain(id, fqdn(domain))) 
      .map {
        case ExistsSuccess(exists) => exists 
        case ExistsFailure(cause)  => throw cause
      }
  }

  /*
   */
  protected def doGenerateAsync(domain: DomainName) = {
    (endpoint ? GenerateWithDomain(domain)) 
      .map {
        case GenerateSuccess(id)     => id 
        case GenerateFailure(cause)  => throw cause
      }
  }

  /*
   */
  protected def doReleaseAsync(id: ID, domain: Option[DomainName]) = domain match {
    case None => doReleaseAsync(id)
    case Some(d) => 
      (endpoint ? ReleaseWithDomain(id, d)) 
        .map {
          case ReleaseSuccess()       => (): Unit
          case ReleaseFailure(cause)  => throw cause
        }
  }
}

trait DomainIDProviderActorRefAdapter[ID] extends IDProviderActorRefAdapter[ID] with DomainIDProviderActorRefAdapterLike[ID] {

  /**
   *
   */
  val protocol: DomainIDProviderProtocol[ID]
}
