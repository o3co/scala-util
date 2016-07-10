package o3co.tag
package store

import akka.actor.Actor
import akka.pattern.pipe
import scala.concurrent.ExecutionContext

/**
 *
 */
trait TagStoreActorLike[O, T <: Tag[T]] {
  this: Actor with TagStoreLike[O, T] => 

  val protocol: TagStoreProtocolLike[O, T]
  import protocol._

  implicit def executionContext: ExecutionContext

  def receiveTagStoreCommand: Receive = {
    case TagExists(owner, tag) => 
      tagExistsAsync(owner, tag)
        .map(exists => TagExistsSuccess(exists))
        .recover {
          case e: Throwable => TagExistsFailure(e)
        }
        .pipeTo(sender())
    case GetTags(owner) => 
      getTagsAsync(owner)
        .map(tags => GetTagsSuccess(tags))
        .recover {
          case e: Throwable => GetTagsFailure(e)
        }
        .pipeTo(sender())
    case GetOwners(tag) => 
      getOwnersAsync(tag)
        .map(tags => GetOwnersSuccess(tags))
        .recover {
          case e: Throwable => GetOwnersFailure(e)
        }
        .pipeTo(sender())
    case PutTag(owner, tag) => 
      putTagAsync(owner, tag)
        .map(_ => PutTagSuccess())
        .recover {
          case e: Throwable => PutTagFailure(e)
        }
        .pipeTo(sender())
    case PutTagSet(tags) => 
      putTagSetAsync(tags)
        .map(_ => PutTagSetSuccess())
        .recover {
          case e: Throwable => PutTagSetFailure(e)
        }
        .pipeTo(sender())
    case DeleteTag(owner, tag) => 
      deleteTagAsync(owner, tag)
        .map(_ => DeleteTagSuccess())
        .recover {
          case e: Throwable => DeleteTagFailure(e)
        }
        .pipeTo(sender())
    case DeleteTagSet(tags) => 
      deleteTagSetAsync(tags)
        .map(_ => DeleteTagSetSuccess())
        .recover {
          case e: Throwable => DeleteTagSetFailure(e)
        }
        .pipeTo(sender())
    case DeleteAllTagsByOwner(owner) => 
      deleteAllTagsAsync(owner)
        .map(_ => DeleteAllTagsSuccess())
        .recover {
          case e: Throwable => DeleteAllTagsFailure(e)
        }
        .pipeTo(sender())
    case DeleteAllTagsByName(tag) => 
      deleteAllTagsAsync(tag)
        .map(_ => DeleteAllTagsSuccess())
        .recover {
          case e: Throwable => DeleteAllTagsFailure(e)
        }
        .pipeTo(sender())
    case ReplaceAllTags(owner, tags) => 
      replaceAllTagsAsync(owner, tags)
        .map(_ => ReplaceAllTagsSuccess())
        .recover {
          case e: Throwable => ReplaceAllTagsFailure(e)
        }
        .pipeTo(sender())
  }
}

