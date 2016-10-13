package o3co.tag
package store

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import o3co.conversion._

/**
 * TagStore wrapper to convert Tag type
 */
trait MappedValueTagStore[OWNER, ITAG <: Tag[ITAG], OTAG <: Tag[OTAG]] extends TagStore[OWNER, OTAG] {

  implicit def executionContext: ExecutionContext
  
  implicit def tagMapper: ReversibleConversion[ITAG, OTAG]

  implicit def reverseTagMapper = tagMapper.reverse

  /**
   *
   */
  def underlying: TagStore[OWNER, ITAG]

  /**
   */
  def getTagsAsync(owner: OWNER) = {
    underlying.getTagsAsync(owner)
      .map(tags => tags.map(tag => tag: OTAG))
  }

  /**
   * Get Set of owners related with the T
   */
  def getOwnersAsync(tag: OTAG) = {
    underlying.getOwnersAsync(tag)
  } //: Future[Set[O]] 

  /**
   *
   */
  def tagExistsAsync(owner: OWNER, tag: OTAG) = {
    underlying.tagExistsAsync(owner, tag)
  } //: Future[Boolean]

  /**
   *
   */
  def putTagAsync(owner: OWNER, tag: OTAG) = {
    underlying.putTagAsync(owner, tag)
  } //: Future[Unit] 

  /**
   */
  def putTagAsync(owner: OWNER, tags: Set[OTAG]) = {
    underlying.putTagAsync(owner, tags.map(tag => tag: ITAG))
  } //: Future[Unit]

  /**
   *
   */
  def putTagSetAsync(tags: Set[(OWNER, OTAG)]) = {
    underlying.putTagSetAsync(tags.map(t => (t._1, t._2: ITAG)))
  } //: Future[Unit] 

  /**
   *
   */
  def deleteTagAsync(owner: OWNER, tag: OTAG): Future[Unit] = {
    underlying.deleteTagAsync(owner, tag)
  } //: Future[Unit]

  /**
   *
   */
  def deleteTagAsync(owner: OWNER, tags: Set[OTAG]): Future[Unit] = {
    underlying.deleteTagAsync(owner, tags.map(tag => tag: ITAG))
  } //: Future[Unit]

  /**
   *
   */
  def deleteTagSetAsync(tags: Set[(OWNER, OTAG)]) = {
    underlying.deleteTagSetAsync(tags.map(t => (t._1, t._2: ITAG)))
  } //: Future[Unit]

  /**
   *
   */
  def deleteAllTagsAsync(owner: OWNER) = {
    underlying.deleteAllTagsAsync(owner)
  } //: Future[Unit]

  /**
   *
   */
  def deleteAllTagsAsync(tag: OTAG) = {
    underlying.deleteAllTagsAsync(tag)
  } //: Future[Unit]

  /**
   *
   */
  def replaceTagsAsync(owner: OWNER, newTags: Set[OTAG], oldTags: Set[OTAG]) = {
    underlying.replaceTagsAsync(owner, newTags.map(tag => tag: ITAG), oldTags.map(t => t: ITAG))
  } //: Future[Unit] 

  /**
   *
   */
  def replaceAllTagsAsync(owner: OWNER, tags: Set[OTAG]) = {
    underlying.replaceAllTagsAsync(owner, tags.map(tag => tag: ITAG))
  } //: Future[Unit] 
}

object MappedValueTagStore {
  
  def apply[OWNER, ITAG <: Tag[ITAG], OTAG <: Tag[OTAG]](store: TagStore[OWNER, ITAG], mapper: ReversibleConversion[ITAG, OTAG])(implicit ec: ExecutionContext) = 
    new MappedValueTagStore[OWNER, ITAG, OTAG] {
      val underlying = store
      val executionContext = ec
      val tagMapper = mapper
    }
}
