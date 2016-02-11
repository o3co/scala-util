package jp.o3co.datastore
package entity

import scala.concurrent.ExecutionContext
import akka.actor.ActorSelection
import akka.util.Timeout
import akka.pattern.ask
import akka.pattern.pipe

/**
 *
 */
trait EntityStoreAdapterLike[K, E <: BaseEntity[K]] extends EntityStore[K, E] {

  val protocol: EntityStoreProtocolLike[EntityKey, Entity]

  import protocol._
  
  def endpoint: ActorSelection

  implicit def executionContext: ExecutionContext

  implicit def timeout: Timeout

  def countEntitiesAsync = {
    (endpoint ? CountEntities)
      .map {
        case CountEntitiesComplete(total)  => total 
        case CountEntitiesFailure(cause)   => throw cause
      }
  }

  def entityExistsAsync(key: EntityKey) = {
    (endpoint ? EntityExists(key))
      .map {
        case EntityExistsComplete(exists) => exists 
        case EntityExistsFailure(cause)   => throw cause
      }
  }

  def getKeysAsync() = {
    (endpoint ? GetKeys())
      .map {
        case GetKeysComplete(keys) => keys 
        case GetKeysFailure(cause) => throw cause
      }
  }

  def getEntityAsync(key: EntityKey) = {
    (endpoint ? GetEntity(key))
      .map {
        case GetEntityComplete(entity) => entity
        case GetEntityFailure(cause)   => throw cause
      }
  }

  def getEntitiesAsync(keys: Seq[EntityKey]) = {
    (endpoint ? GetEntities(keys))
      .map {
        case GetEntitiesComplete(entities) => entities
        case GetEntitiesFailure(cause)   => throw cause
      }
  }

  def putEntityAsync(entity: Entity) = {
    (endpoint ? PutEntity(entity))
      .map {
        case PutEntityComplete(prev) => prev 
        case PutEntityFailure(cause) => throw cause
      }
  }

  def deleteEntityAsync(key: EntityKey) = {
    (endpoint ? DeleteEntity(key))
      .map {
        case DeleteEntityComplete(deleted) => deleted 
        case DeleteEntityFailure(cause)    => throw cause
      }
  }
}
