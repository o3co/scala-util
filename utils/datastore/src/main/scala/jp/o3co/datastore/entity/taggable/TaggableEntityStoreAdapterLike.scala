package jp.o3co.datastore
package entity
package taggable 

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import akka.actor.ActorSelection
import akka.util.Timeout
import akka.pattern.ask
import akka.pattern.pipe

import jp.o3co.tag.{TagName, TagNameSet}

/**
 *
 */
trait TaggableEntityStoreAdapter[K, E <: BaseEntity[K]] extends EntityStoreAdapterLike[K, E] with TaggableEntityStoreAdapterLike[K, E] {
  
  val protocol: EntityStoreProtocolLike[K, E] with TaggableEntityStoreProtocolLike[K, E]
}

/**
 *
 */
trait TaggableEntityStoreAdapterLike[Key, Entity <: BaseEntity[Key]] extends TaggableEntityStoreLike[Key, Entity] {

  val protocol: TaggableEntityStoreProtocolLike[Key, Entity]

  import protocol._

  def endpoint: ActorSelection

  implicit def executionContext: ExecutionContext

  implicit def timeout: Timeout
  
  def getEntityWithTagsAsync(key: Key) = {
    (endpoint ? GetEntityWithTags(key))
      .map {
        case GetEntityWithTagsSuccess(Some(created), tags) => Option((created, tags)): Option[(Entity, TagNameSet)]
        case GetEntityWithTagsSuccess(None, _)  => None
        case GetEntityWithTagsFailure(cause)     => throw cause
      }
  }

  def putEntityWithTagsAsync(entity: Entity, tags: TagNameSet) = {

    (endpoint ? PutEntityWithTags(entity, tags))
      .map {
        case PutEntityWithTagsSuccess()      => (): Unit
        case PutEntityWithTagsFailure(cause)      => throw cause
      }
  }

  def deleteEntityWithTagsAsync(key: Key) = {
    (endpoint ? DeleteEntityWithTags(key))
      .map {
        case DeleteEntityWithTagsSuccess()  => (): Unit 
        case DeleteEntityWithTagsFailure(cause)     => throw cause
      }
  }
}

