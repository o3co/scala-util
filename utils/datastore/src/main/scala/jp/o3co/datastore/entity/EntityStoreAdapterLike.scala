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
        case CountEntitiesSuccess(total)  => total 
        case CountEntitiesFailure(cause)   => throw cause
      }
  }

  def entityExistsAsync(key: EntityKey) = {
    (endpoint ? EntityExists(key))
      .map {
        case EntityExistsSuccess(exists) => exists 
        case EntityExistsFailure(cause)   => throw cause
      }
  }

  def getKeysAsync() = {
    (endpoint ? GetKeys())
      .map {
        case GetKeysSuccess(keys) => keys 
        case GetKeysFailure(cause) => throw cause
      }
  }

  def getEntityAsync(key: EntityKey) = {
    (endpoint ? GetEntity(key))
      .map {
        case GetEntitySuccess(entity) => entity
        case GetEntityFailure(cause)   => throw cause
      }
  }

  def getEntitiesAsync(keys: Seq[EntityKey]) = {
    (endpoint ? GetEntities(keys))
      .map {
        case GetEntitiesSuccess(entities) => entities
        case GetEntitiesFailure(cause)   => throw cause
      }
  }

  def putEntityAsync(entity: Entity) = {
    (endpoint ? PutEntity(entity))
      .map {
        case PutEntitySuccess() => (): Unit 
        case PutEntityFailure(cause) => throw cause
      }
  }

  def deleteEntityAsync(key: EntityKey) = {
    (endpoint ? DeleteEntity(key))
      .map {
        case DeleteEntitySuccess() => (): Unit 
        case DeleteEntityFailure(cause)    => throw cause
      }
  }
}

trait EntityStoreAdapter[K, E <: BaseEntity[K]] extends EntityStoreAdapterLike[K, E]
