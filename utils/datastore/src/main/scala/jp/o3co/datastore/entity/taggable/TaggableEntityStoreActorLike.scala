package jp.o3co.datastore
package entity
package taggable 

import akka.actor.Actor
import akka.pattern.pipe
import scala.concurrent.ExecutionContext

import jp.o3co.tag.store.TagStoreActorLike
import jp.o3co.tag.store.TagStoreLike
import jp.o3co.tag.store.TagStoreProtocolLike


trait TaggableEntityStoreActor[K, E <: BaseEntity[K]] extends EntityStoreActorLike[K, E]
  with TagStoreActorLike[K]
  with TaggableEntityStoreActorLike[K, E]
{
  this: Actor with EntityStoreLike[K, E] with TagStoreLike[K] with TaggableEntityStoreLike[K, E] =>

  val protocol: EntityStoreProtocolLike[K, E] with TagStoreProtocolLike[K] with TaggableEntityStoreProtocolLike[K, E]

  def receiveOther: Receive = {
    case x => 
      throw new Exception(s"Unsupported event $x is received")
  }

  def receive: Receive = receiveTaggableEntityStoreCommand
        .orElse(receiveEntityStoreCommand)
        .orElse(receiveTagStoreCommand)
        .orElse(receiveExtensions)
}

/**
 *
 */
trait TaggableEntityStoreActorLike[K, E <: BaseEntity[K]] {
  this: Actor with TaggableEntityStoreLike[K, E] => 

  val protocol: TaggableEntityStoreProtocolLike[K, E]

  import protocol._

  implicit def executionContext: ExecutionContext 

  def receiveTaggableEntityStoreCommand: Receive = {
    case GetEntityWithTags(key) => 
      getEntityWithTagsAsync(key)
        .map {
          case Some((entity, tags)) => GetEntityWithTagsComplete(Option(entity), tags)
          case _                    => GetEntityWithTagsComplete(None)
        }
        .recover {
          case e: Throwable => GetEntityWithTagsFailure(e)
        }
        .pipeTo(sender)
    case PutEntityWithTags(entity, tags) => 
      putEntityWithTagsAsync(entity, tags)
        .map(prev => PutEntityWithTagsComplete(prev))
        .recover {
          case e: Throwable => PutEntityWithTagsFailure(e)
        }
        .pipeTo(sender)
    case DeleteEntityWithTags(key) => 
      deleteEntityWithTagsAsync(key)
        .map(deleted  => DeleteEntityWithTagsComplete(deleted))
        .recover {
          case e: Throwable => DeleteEntityWithTagsFailure(e)
        }
        .pipeTo(sender)
  }
}

