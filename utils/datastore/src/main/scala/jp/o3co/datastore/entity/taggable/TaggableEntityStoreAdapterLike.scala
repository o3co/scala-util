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
import jp.o3co.tag.store.{TagStoreAdapterLike, TagStoreProtocolLike}


trait TaggableEntityStoreAdapter[K, E <: BaseEntity[K]] extends EntityStoreAdapterLike[K, E] with TagStoreAdapterLike[K] with TaggableEntityStoreAdapterLike[K, E] {
  
  val protocol: EntityStoreProtocolLike[K, E] with TagStoreProtocolLike[K] with TaggableEntityStoreProtocolLike[K, E]
}

/**
 *
 */
trait TaggableEntityStoreAdapterLike[K, E <: BaseEntity[K]] extends TaggableEntityStoreLike[K, E] {
  //with EntityStoreAdapterLike[K, E] with TagStoreAdapterLike[K] {

  val protocol: TaggableEntityStoreProtocolLike[EntityKey, E]

  import protocol._

  def endpoint: ActorSelection

  implicit def executionContext: ExecutionContext

  implicit def timeout: Timeout
  
  def getEntityWithTagsAsync(key: EntityKey) = {
    (endpoint ? GetEntityWithTags(key))
      .map {
        case GetEntityWithTagsComplete(Some(created), tags) => Option((created, tags)): Option[(Entity, TagNameSet)]
        case GetEntityWithTagsComplete(None, _)  => None
        case GetEntityWithTagsFailure(cause)     => throw cause
      }
  }

  def putEntityWithTagsAsync(entity: E, tags: TagNameSet) = {

    (endpoint ? PutEntityWithTags(entity, tags))
      .map {
        case PutEntityWithTagsComplete(prev)      => prev
        case PutEntityWithTagsFailure(cause)      => throw cause
      }
  }

  def deleteEntityWithTagsAsync(key: EntityKey) = {
    (endpoint ? DeleteEntityWithTags(key))
      .map {
        case DeleteEntityWithTagsComplete(deleted)  => deleted 
        case DeleteEntityWithTagsFailure(cause)     => throw cause
      }
  }
}

