package o3co.tag
package store

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import o3co.tag.filter.TagFilter

/**
 * Proxy implementation of TagStore with TagFilter
 */
trait FilterTagStore[OWNER, TAG <: Tag[TAG]] extends TagStore[OWNER, TAG] {

  implicit def executionContext: ExecutionContext

  /**
   *
   */
  def underlying: TagStore[OWNER, TAG]

  /**
   *  
   */
  def tagFilter: TagFilter[TAG]

  /**
   * Get Set of T related with the owner
   */
  def getTagsAsync(owner: OWNER) = {
    underlying.getTagsAsync(owner)
      .map { tags => 
        tagFilter(tags)
      }
  }

  /**
   * Get Set of owners related with the T
   */
  def getOwnersAsync(tag: TAG) = {
    tagFilter.reverse(tag) match {
      case Some(t) => underlying.getOwnersAsync(t)
      case None => Future.failed(new IllegalArgumentException(s"Invalid tag '$tag' is specified."))
    }
  }

  /**
   *
   */
  def tagExistsAsync(owner: OWNER, tag: TAG) = {
    tagFilter.reverse(tag) match {
      case Some(t) => underlying.tagExistsAsync(owner, t)
      case None => Future.failed(new IllegalArgumentException(s"Invalid tag '$tag' is specified."))
    }
  }

  /**
   *
   */
  def putTagAsync(owner: OWNER, tag: TAG) = {
    tagFilter.reverse(tag) match {
      case Some(t) => underlying.putTagAsync(owner, t)
      case None => Future.failed(new IllegalArgumentException(s"Invalid tag '$tag' is specified."))
    }
  }

  /**
   */
  def putTagAsync(owner: OWNER, tags: Set[TAG]) = {
    underlying.putTagAsync(owner, tagFilter.reverse(tags))
  }

  /**
   *
   */
  def putTagSetAsync(tags: Set[(OWNER, TAG)]) = {
    underlying.putTagSetAsync(
      tags
        .map(t => (t._1, tagFilter.reverse(t._2)))
        .collect {
          case (owner, Some(t)) => (owner, t)
        }
    )
  }

  /**
   *
   */
  def deleteTagAsync(owner: OWNER, tag: TAG) = {
    tagFilter.reverse(tag) match {
      case Some(t) => underlying.deleteTagAsync(owner, t)
      case None => Future.failed(new IllegalArgumentException(s"Invalid tag '$tag' is specified."))
    }
  }

  def deleteTagAsync(owner: OWNER, tags: Set[TAG]) = {
    underlying.deleteTagAsync(owner, tagFilter.reverse(tags))
  }

  /**
   *
   */
  def deleteTagSetAsync(tags: Set[(OWNER, TAG)]) = {
    underlying.deleteTagSetAsync(
      tags
        .map(t => (t._1, tagFilter.reverse(t._2)))
        .collect {
          case (o, Some(t)) => (o, t) 
        }
    )
  }

  /**
   *
   */
  def deleteAllTagsAsync(owner: OWNER) = {
    getTagsAsync(owner).flatMap { tags => 
      deleteTagAsync(owner, tags)
    }
  }

  /**
   *
   */
  def deleteAllTagsAsync(tag: TAG) = {
    tagFilter.reverse(tag) match {
      case Some(t) => underlying.deleteAllTagsAsync(t)
      case None    => Future.failed(new IllegalArgumentException(s"Invalid tag '$tag' is specified."))
    }
  }

  /**
   *
   */
  def replaceAllTagsAsync(owner: OWNER, newTags: Set[TAG]) = {
    getTagsAsync(owner).flatMap { tags => 
      replaceTagsAsync(owner, newTags, tags)
    }
  }

  /**
   *
   */
  def replaceTagsAsync(owner: OWNER, newTags: Set[TAG], oldTags: Set[TAG]) = {
    underlying.replaceTagsAsync(owner, tagFilter.reverse(newTags), tagFilter.reverse(oldTags))
  }
}

object FilterTagStore {

  /**
   *
   */
  def apply[K, V <: Tag[V]](store: TagStore[K, V], filter: TagFilter[V])(implicit ec: ExecutionContext): FilterTagStore[K, V] = 
    new FilterTagStore[K, V] {
      val executionContext = ec
      val underlying = store
      val tagFilter = filter 
    }
}
