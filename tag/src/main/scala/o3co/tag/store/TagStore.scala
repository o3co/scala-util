package o3co.tag
package store 

import scala.concurrent.Future

/**
 * trait TagStore to store tags with extended MultiMap(SetMultiMap)[O, T] strategy to hold tags for key.
 */
trait TagStore[O, T <: Tag[T]] {
  /**
   * Get Set of T related with the owner
   */
  def getTagsAsync(owner: O): Future[Set[T]]

  /**
   * Get Set of owners related with the T
   */
  def getOwnersAsync(tag: T): Future[Set[O]] 

  /**
   *
   */
  def tagExistsAsync(owner: O, tag: T): Future[Boolean]

  /**
   *
   */
  def putTagAsync(owner: O, tag: T): Future[Unit] 

  /**
   */
  def putTagAsync(owner: O, tags: Set[T]): Future[Unit]

  /**
   *
   */
  def putTagSetAsync(tags: Set[(O, T)]): Future[Unit] 

  /**
   *
   */
  def deleteTagAsync(owner: O, tag: T): Future[Unit]

  /**
   *
   */
  def deleteTagAsync(owner: O, tags: Set[T]): Future[Unit]

  /**
   *
   */
  def deleteTagSetAsync(tags: Set[(O, T)]): Future[Unit]

  /**
   *
   */
  def deleteAllTagsAsync(owner: O): Future[Unit]

  /**
   *
   */
  def deleteAllTagsAsync(tag: T): Future[Unit]

  /**
   *
   */
  def replaceTagsAsync(owner: O, newTags: Set[T], oldTags: Set[T]): Future[Unit] 

  /**
   *
   */
  def replaceAllTagsAsync(owner: O, newTags: Set[T]): Future[Unit] 
}
