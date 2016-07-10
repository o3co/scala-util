package jp.o3co.id.provider

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.ExecutionContext

/**
 *
 */
trait IDProviderActorRefAdapterLike[ID] extends IDProviderAdapterLike[ID] {
  
  val protocol: IDProviderProtocolLike[ID]
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
  protected def doExistsAsync(id: ID) = {
    (endpoint ? Exists(id)) 
      .map {
        case ExistsSuccess(exists) => exists 
        case ExistsFailure(cause)  => throw cause
      }
  }

  /**
   * Create new ID 
   */
  protected def doGenerateAsync() = {
    (endpoint ? Generate()) 
      .map {
        case GenerateSuccess(id)     => id 
        case GenerateFailure(cause)  => throw cause
      }
  }

  /**
   *
   */
  protected def doReleaseAsync(id: ID) = {
    (endpoint ? Release(id)) 
      .map {
        case ReleaseSuccess()       => (): Unit
        case ReleaseFailure(cause)  => throw cause
      }
  }
}

trait IDProviderActorRefAdapter[ID] extends IDProviderActorRefAdapterLike[ID] 

