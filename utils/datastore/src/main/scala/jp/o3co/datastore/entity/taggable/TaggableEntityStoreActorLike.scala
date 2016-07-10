package jp.o3co.datastore
package entity
package taggable 

import akka.actor.Actor
import akka.pattern.pipe
import scala.concurrent.ExecutionContext


trait TaggableEntityStoreActor[K, E <: BaseEntity[K]] extends Actor 
  with EntityStoreActorLike[K, E]
  with TaggableEntityStoreActorLike[K, E]
{
  this: EntityStoreLike[K, E] with TaggableEntityStoreLike[K, E] =>

  val protocol: EntityStoreProtocolLike[K, E] with TaggableEntityStoreProtocolLike[K, E]

  //override def receiveExtensions: Receive = Actor.emptyBehavior

  def receive: Receive = receiveTaggableEntityStoreCommand
        .orElse(receiveEntityStoreCommand)
        .orElse(receiveExtensions)
}

/**
 *
 */
trait TaggableEntityStoreActorLike[K, E <: BaseEntity[K]] {
  this: Actor with TaggableEntityStoreLike[K, E] => 

  // Require protocol to implement TaggableEntityStoreProtocolLike
  val protocol: TaggableEntityStoreProtocolLike[K, E]

  import protocol._

  implicit def executionContext: ExecutionContext 

  /**
   *
   */
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
        .map(_ => PutEntityWithTagsSuccess())
        .recover {
          case e: Throwable => PutEntityWithTagsFailure(e)
        }
        .pipeTo(sender)
    case DeleteEntityWithTags(key) => 
      deleteEntityWithTagsAsync(key)
        .map(_ => DeleteEntityWithTagsSuccess())
        .recover {
          case e: Throwable => DeleteEntityWithTagsFailure(e)
        }
        .pipeTo(sender)
  }
}

