package jp.o3co.datastore
package entity
package taggable 

import akka.actor.Actor
import akka.pattern.pipe
import scala.concurrent.ExecutionContext

import jp.o3co.tag.owner.TagOwnerActorLike
import jp.o3co.tag.owner.TagOwnerLike
import jp.o3co.tag.owner.TagOwnerProtocolLike


trait TaggableEntityStoreActor[K, E <: BaseEntity[K]] extends EntityStoreActorLike[K, E]
  with TagOwnerActorLike[K]
  with TaggableEntityStoreActorLike[K, E]
{
  this: Actor with EntityStoreLike[K, E] with TagOwnerLike[K] with TaggableEntityStoreLike[K, E] =>

  val protocol: EntityStoreProtocolLike[K, E] with TagOwnerProtocolLike[K] with TaggableEntityStoreProtocolLike[K, E]

  //override def receiveExtensions: Receive = Actor.emptyBehavior

  def receive: Receive = receiveTaggableEntityStoreCommand
        .orElse(receiveEntityStoreCommand)
        .orElse(receiveTagOwnerCommand)
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
          case Some((entity, tags)) => GetEntityWithTagsSuccess(Option(entity), tags)
          case _                    => GetEntityWithTagsSuccess(None)
        }
        .recover {
          case e: Throwable => GetEntityWithTagsFailure(e)
        }
        .pipeTo(sender)
    case PutEntityWithTags(entity, tags) => 
      putEntityWithTagsAsync(entity, tags)
        .map(prev => PutEntityWithTagsSuccess(prev))
        .recover {
          case e: Throwable => PutEntityWithTagsFailure(e)
        }
        .pipeTo(sender)
    case DeleteEntityWithTags(key) => 
      deleteEntityWithTagsAsync(key)
        .map(deleted  => DeleteEntityWithTagsSuccess(deleted))
        .recover {
          case e: Throwable => DeleteEntityWithTagsFailure(e)
        }
        .pipeTo(sender)
  }
}

