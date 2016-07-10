package o3co.id.domain.provider

import akka.actor.ActorSelection
import akka.pattern.ask
import akka.util.Timeout
import o3co.net.Domain
import scala.concurrent.ExecutionContext
import o3co.id.{provider => base}

/**
 *
 */
trait ActorSelectionIdProviderLike[ID] extends base.ActorSelectionIdProviderLike[ID] {
  this: IdProvider[ID] => 
  
  /**
   *
   */
  val protocol: IdProviderProtocol[ID] 
  import protocol._

  /**
   *
   */
  def endpoint: ActorSelection

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
  def existsAsync(id: ID, domain: Domain) = {
    (endpoint ? ExistsOnDomain(id, domain)) 
      .map {
        case ExistsSuccess(exists) => exists 
        case ExistsFailure(cause)  => throw cause
      }
  }

  /*
   */
  def generateAsync(domain: Domain) = {
    (endpoint ? GenerateOnDomain(domain)) 
      .map {
        case GenerateSuccess(id)     => id 
        case GenerateFailure(cause)  => throw cause
      }
  }

  /*
   */
  def reserveAsync(id: ID, domain: Domain) = {
    (endpoint ? ReserveOnDomain(id, domain)) 
      .map {
        case ReserveSuccess        => //
        case ReserveFailure(cause) => throw cause
      }
  }

  /*
   */
  def releaseAsync(id: ID, domain: Domain) = {
    (endpoint ? ReleaseOnDomain(id, domain)) 
      .map {
        case ReleaseSuccess        => //
        case ReleaseFailure(cause) => throw cause
      }
  }
}

trait ActorSelectionIdProvider[ID] extends IdProvider[ID] with ActorSelectionIdProviderLike[ID] 
