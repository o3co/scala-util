package jp.o3co.tag
package store
package impl 

import akka.actor.ActorRef 
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.ExecutionContext

/**
 *  
 */
trait TagStoreActorRefAdapterLike[Owner] extends TagStore[Owner] {

  val protocol: TagStoreProtocolLike[Owner]

  import protocol._

  def endpoint: ActorRef

  implicit def timeout: Timeout

  implicit def executionContext: ExecutionContext

  def getTagsForAsync(owner: Owner) = {
    (endpoint ? GetTagsFor(owner))
      .map {
        case GetTagsForSuccess(names) => names
        case GetTagsForFailure(cause) => throw cause
      }
  }

  def getOwnersForAsync(name: TagName) = {
    (endpoint ? GetOwnersFor(name))
      .map {
        case GetOwnersForSuccess(owners) => owners
        case GetOwnersForFailure(cause) => throw cause
      }
  }

  def tagExistsAsync(owner: Owner, name: TagName) = {
    (endpoint ? TagExists(owner, name))
      .map {
        case TagExistsSuccess(exists) => exists 
        case TagExistsFailure(cause) => throw cause
      }
  }

  def putTagAsync(owner: Owner, name: TagName) = {
    (endpoint ? PutTag(owner, name))
      .map {
        case PutTagSuccess() => ()
        case PutTagFailure(cause) => throw cause
      }
  }

  def putTagSetAsync(tags: Set[(Owner, TagName)]) = {
    (endpoint ? PutTagSet(tags))
      .map {
        case PutTagSetSuccess() => ()
        case PutTagSetFailure(cause) => throw cause
      }
  }

  def deleteTagAsync(owner: Owner, name: TagName) = {
    (endpoint ? DeleteTag(owner, name))
      .map {
        case DeleteTagSuccess() => ()
        case DeleteTagFailure(cause) => throw cause
      }
  }

  def deleteTagSetAsync(tags: Set[(Owner, TagName)]) = {
    (endpoint ? DeleteTagSet(tags))
      .map {
        case DeleteTagSetSuccess() => ()
        case DeleteTagSetFailure(cause) => throw cause
      }
  }

  def deleteAllTagsAsync(owner: Owner) = {
    (endpoint ? DeleteAllTagsByOwner(owner))
      .map {
        case DeleteAllTagsSuccess() => ()
        case DeleteAllTagsFailure(cause) => throw cause
      }
  }

  def deleteAllTagsAsync(name: TagName) = {
    (endpoint ? DeleteAllTagsByName(name))
      .map {
        case DeleteAllTagsSuccess() => ()
        case DeleteAllTagsFailure(cause) => throw cause
      }
  }

  def replaceAllTagsAsync(owner: Owner, names: TagNameSet) = {
    (endpoint ? ReplaceAllTags(owner, names))
      .map {
        case ReplaceAllTagsSuccess() => ()
        case ReplaceAllTagsFailure(cause) => throw cause
      }
  }
}

