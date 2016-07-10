package o3co.id.provider

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.ExecutionContext

/**
 *
 */
trait ActorRefIdProviderLike[ID] {
  this: IdProvider[ID] => 

  val protocol: IdProviderProtocol[ID]
  import protocol._

  /*
   */
  def endpoint: ActorRef

  /*
   */
  implicit def timeout: Timeout

  /*
   */
  implicit def executionContext: ExecutionContext

  /**
   *
   */
  def existsAsync(id: ID) = {
    (endpoint ? Exists(id)) 
      .map {
        case ExistsSuccess(exists) => exists 
        case ExistsFailure(cause)  => throw cause
      }
  }

  /**
   * Create new ID 
   */
  def generateAsync() = {
    (endpoint ? Generate) 
      .map {
        case GenerateSuccess(id)     => id 
        case GenerateFailure(cause)  => throw cause
      }
  }

  /**
   *
   */
  def reserveAsync(id: ID) = {
    (endpoint ? Reserve(id)) 
      .map {
        case ReserveSuccess         => //
        case ReserveFailure(cause)  => throw cause
      }
  }

  /**
   *
   */
  def releaseAsync(id: ID) = {
    (endpoint ? Release(id)) 
      .map {
        case ReleaseSuccess         => //
        case ReleaseFailure(cause)  => throw cause
      }
  }
}

trait ActorRefIdProvider[ID] extends IdProvider[ID] with ActorRefIdProviderLike[ID] 

