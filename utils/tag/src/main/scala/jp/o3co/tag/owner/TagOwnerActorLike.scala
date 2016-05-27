package jp.o3co.tag
package owner

import akka.actor.Actor
import akka.pattern.pipe
import scala.concurrent.ExecutionContext

trait TagOwnerActorLike[O] extends TagOwnerComponents[O] {
  this: Actor with TagOwnerLike[O] => 

  val protocol: TagOwnerProtocolLike[O]

  import protocol._

  implicit def executionContext: ExecutionContext

  def receiveTagOwnerCommand: Receive = {
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
        .map(updated => AddTagsComplete())
        .recover {
          case e: Throwable => AddTagsFailure(e)
        }
        .pipeTo(sender)
    case RemoveTags(owner, tags) => 
      removeTags(owner, tags)
        .map(updated => RemoveTagsComplete())
        .recover {
          case e: Throwable => RemoveTagsFailure(e)
        }
        .pipeTo(sender)
    case RemoveAllTags(owner) => 
      removeAllTags(owner)
        .map(removed => RemoveAllTagsComplete())
        .recover {
          case e: Throwable => RemoveAllTagsFailure(e)
        }
        .pipeTo(sender)
    case ReplaceTags(owner, tags) => 
      replaceTags(owner, tags)
        .map(removed => ReplaceTagsComplete())
        .recover {
          case e: Throwable => ReplaceTagsFailure(e)
        }
        .pipeTo(sender)
  }
}

