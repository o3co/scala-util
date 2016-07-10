package jp.o3co.datastore
package entity

import akka.actor.Actor
import akka.pattern.pipe
import scala.concurrent.ExecutionContext


trait EntityStoreActor[K, E <: BaseEntity[K]] extends EntityStoreActorLike[K, E] {
  this: Actor with EntityStoreLike[K, E] => 

  def receive = receiveEntityStoreCommand orElse receiveExtensions

}

trait EntityStoreActorLike[K, E <: BaseEntity[K]] extends EntityStoreComponents[K, E] {
  this: Actor with EntityStoreLike[K, E] => 

  val protocol: EntityStoreProtocolLike[K, E]

  import protocol._

  implicit def executionContext: ExecutionContext 

  def receiveExtensions: Receive = Actor.emptyBehavior

  def receiveEntityStoreCommand: Receive = {
    case EntityExists(key) => 
      entityExistsAsync(key)
        .map(exists => EntityExistsSuccess(exists))
        .recover {
          case e: Throwable => EntityExistsFailure(e)
        }
        .pipeTo(sender())
    case GetKeys() => 
      getKeysAsync()
        .map(keys => GetKeysSuccess(keys))
        .recover {
          case e: Throwable => GetKeysFailure(e)
        }
        .pipeTo(sender())
    case GetEntity(key) => 
      getEntityAsync(key)
        .map(entity => GetEntitySuccess(entity))
        .recover {
          case e: Throwable => GetEntityFailure(e)
        }
        .pipeTo(sender())
    case GetEntities(keys) =>
      getEntitiesAsync(keys)
        .map(entities => GetEntitiesSuccess(entities))
        .recover {
          case e: Throwable => GetEntitiesFailure(e)
        }
        .pipeTo(sender())
    case PutEntity(entity) => 
      putEntityAsync(entity)
        .map(_ => PutEntitySuccess())
        .recover {
          case e: Throwable => PutEntityFailure(e)
        }
        .pipeTo(sender())
    case DeleteEntity(key) => 
      deleteEntityAsync(key)
        .map(_ => DeleteEntitySuccess())
        .recover {
          case e: Throwable => DeleteEntityFailure(e)
        }
        .pipeTo(sender())
  }
}
