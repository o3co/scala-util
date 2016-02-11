package jp.o3co.tag
package store

import akka.actor.Actor
import akka.pattern.pipe
import scala.concurrent.ExecutionContext

trait TagStoreActorLike[O] extends TagStoreComponents[O] {
  this: Actor with TagStoreLike[O] => 

  val protocol: TagStoreProtocolLike[O]

  import protocol._

  implicit def executionContext: ExecutionContext

  def receiveTagStoreCommand: Receive = {
    case ContainsTag(owner, tag) => 
      containsTag(owner, tag)
        .map(exists => ContainsTagComplete(exists))
        .recover {
          case e: Throwable => ContainsTagFailure(e)
        }
        .pipeTo(sender)
    case GetTags(owner) => 
      getTags(owner)
        .map(tags => GetTagsComplete(tags))
        .recover {
          case e: Throwable => GetTagsFailure(e)
        }
        .pipeTo(sender)
    case AddTags(owner, tags) => 
      addTags(owner, tags)
        .map(updated => AddTagsComplete(updated))
        .recover {
          case e: Throwable => AddTagsFailure(e)
        }
        .pipeTo(sender)
    case RemoveTags(owner, tags) => 
      removeTags(owner, tags)
        .map(updated => RemoveTagsComplete(updated))
        .recover {
          case e: Throwable => RemoveTagsFailure(e)
        }
        .pipeTo(sender)
    case RemoveAllTags(owner) => 
      removeAllTags(owner)
        .map(removed => RemoveAllTagsComplete(removed))
        .recover {
          case e: Throwable => RemoveAllTagsFailure(e)
        }
        .pipeTo(sender)
    case ReplaceTags(owner, tags) => 
      replaceTags(owner, tags)
        .map(removed => ReplaceTagsComplete(removed))
        .recover {
          case e: Throwable => ReplaceTagsFailure(e)
        }
        .pipeTo(sender)
  }
}

