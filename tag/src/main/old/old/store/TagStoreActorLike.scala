package jp.o3co.tag
package store

import akka.actor.Actor
import akka.pattern.pipe
import scala.concurrent.ExecutionContext

trait TagStoreActorLike[Owner] {
  this: Actor with TagStoreLike[Owner] => 

  val protocol: TagStoreProtocolLike[Owner]

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
    case GetTagsFor(owner) => 
      getTagsForAsync(owner)
        .map(tags => GetTagsForSuccess(tags))
        .recover {
          case e: Throwable => GetTagsForFailure(e)
        }
        .pipeTo(sender())
    case GetOwnersFor(name) => 
      getOwnersForAsync(name)
        .map(tags => GetOwnersForSuccess(tags))
        .recover {
          case e: Throwable => GetOwnersForFailure(e)
        }
        .pipeTo(sender())
    case PutTag(owner, name) => 
      putTagAsync(owner, name)
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
    case DeleteTag(owner, name) => 
      deleteTagAsync(owner, name)
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
    case DeleteAllTagsByName(name) => 
      deleteAllTagsAsync(name)
        .map(_ => DeleteAllTagsSuccess())
        .recover {
          case e: Throwable => DeleteAllTagsFailure(e)
        }
        .pipeTo(sender())
    case ReplaceAllTags(owner, names) => 
      replaceAllTagsAsync(owner, names)
        .map(_ => ReplaceAllTagsSuccess())
        .recover {
          case e: Throwable => ReplaceAllTagsFailure(e)
        }
        .pipeTo(sender())
  }
}

