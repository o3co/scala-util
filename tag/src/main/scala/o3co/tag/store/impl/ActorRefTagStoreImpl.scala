package o3co.tag
package store
package impl 

import akka.actor.ActorRef 
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.ExecutionContext

/**
 *  
 */
trait ActorRefTagStoreImpl[O, T <: Tag[T]] extends TagStore[O, T] {

  val protocol: TagStoreProtocol[O, T]

  import protocol._

  def endpoint: ActorRef

  implicit def timeout: Timeout

  implicit def executionContext: ExecutionContext

  def getTagsAsync(owner: O) = {
    (endpoint ? GetTags(owner))
      .map {
        case GetTagsSuccess(tags) => tags
        case GetTagsFailure(cause) => throw cause
      }
  }

  def getOwnersAsync(tag: T) = {
    (endpoint ? GetOwners(tag))
      .map {
        case GetOwnersSuccess(owners) => owners
        case GetOwnersFailure(cause) => throw cause
      }
  }

  def tagExistsAsync(owner: O, tag: T) = {
    (endpoint ? TagExists(owner, tag))
      .map {
        case TagExistsSuccess(exists) => exists 
        case TagExistsFailure(cause) => throw cause
      }
  }

  def putTagAsync(owner: O, tag: T) = {
    (endpoint ? PutTag(owner, tag))
      .map {
        case PutTagSuccess() => ()
        case PutTagFailure(cause) => throw cause
      }
  }

  def putTagSetAsync(tags: Set[(O, T)]) = {
    (endpoint ? PutTagSet(tags))
      .map {
        case PutTagSetSuccess() => ()
        case PutTagSetFailure(cause) => throw cause
      }
  }

  def deleteTagAsync(owner: O, tag: T) = {
    (endpoint ? DeleteTag(owner, tag))
      .map {
        case DeleteTagSuccess() => ()
        case DeleteTagFailure(cause) => throw cause
      }
  }

  def deleteTagSetAsync(tags: Set[(O, T)]) = {
    (endpoint ? DeleteTagSet(tags))
      .map {
        case DeleteTagSetSuccess() => ()
        case DeleteTagSetFailure(cause) => throw cause
      }
  }

  def deleteAllTagsAsync(owner: O) = {
    (endpoint ? DeleteAllTagsByOwner(owner))
      .map {
        case DeleteAllTagsSuccess() => ()
        case DeleteAllTagsFailure(cause) => throw cause
      }
  }

  def deleteAllTagsAsync(tag: T) = {
    (endpoint ? DeleteAllTagsByName(tag))
      .map {
        case DeleteAllTagsSuccess() => ()
        case DeleteAllTagsFailure(cause) => throw cause
      }
  }

  def replaceAllTagsAsync(owner: O, tags: Set[T]) = {
    (endpoint ? ReplaceAllTags(owner, tags))
      .map {
        case ReplaceAllTagsSuccess() => ()
        case ReplaceAllTagsFailure(cause) => throw cause
      }
  }
}

