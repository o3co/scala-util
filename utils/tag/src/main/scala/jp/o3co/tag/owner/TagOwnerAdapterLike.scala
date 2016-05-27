package jp.o3co.tag
package owner

import akka.actor.ActorSelection 
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.ExecutionContext

trait TagOwnerAdapterLike[O] extends TagOwner[O] {

  val protocol: TagOwnerProtocolLike[O]

  import protocol._

  def endpoint: ActorSelection

  implicit def timeout: Timeout

  implicit def executionContext: ExecutionContext

  def containsTag(owner: Owner, tag: TagName) = {
    (endpoint ? ContainsTag(owner, tag))
      .map {
        case ContainsTagComplete(exists) => exists
        case ContainsTagFailure(cause)   => throw cause
      }
  }
 
  def getTags(owner: Owner) = {
    (endpoint ? GetTags(owner))
      .map {
        case GetTagsComplete(tags)   => tags 
        case GetTagsFailure(cause)   => throw cause
      }
  }
  
  def addTags(owner: Owner, tags: TagNameSet) = {
    (endpoint ? AddTags(owner, tags))
      .map {
        case AddTagsComplete() => ()
        case AddTagsFailure(cause)    => throw cause
      }
  }

  def removeTags(owner: Owner, tags: TagNameSet) = {
    (endpoint ? RemoveTags(owner, tags))
      .map {
        case RemoveTagsComplete() => ()
        case RemoveTagsFailure(cause)    => throw cause
      }
  }

  def removeAllTags(owner: Owner) = {
    (endpoint ? RemoveAllTags(owner))
      .map {
        case RemoveAllTagsComplete() => ()
        case RemoveAllTagsFailure(cause)    => throw cause
      }
  }

  def replaceTags(owner: Owner, tags: TagNameSet) = {
    (endpoint ? ReplaceTags(owner, tags))
      .map {
        case ReplaceTagsComplete() => ()
        case ReplaceTagsFailure(cause)    => throw cause
      }
  }
}
