package jp.o3co.tag
package store 

import scala.concurrent.Future

/**
 * TagStore is to store tags with owner identity.
 */
trait TagStore[O] extends TagStoreLike[O]

/**
 *
 */
trait TagStoreLike[Owner] {
  /**
   *
   */
  def getTagsForAsync(owner: Owner): Future[TagNameSet]

  /**
   *
   */
  def getOwnersForAsync(name: TagName): Future[Set[Owner]] 

  /**
   *
   */
  def tagExistsAsync(owner: Owner, name: TagName): Future[Boolean]

  /**
   *
   */
  def putTagAsync(owner: Owner, name: TagName): Future[Unit] 

  /**
   *
   */
  def putTagSetAsync(tags: Set[(Owner, TagName)]): Future[Unit] 

  /**
   *
   */
  def deleteTagAsync(owner: Owner, name: TagName): Future[Unit]

  /**
   *
   */
  def deleteTagSetAsync(tags: Set[(Owner, TagName)]): Future[Unit]

  /**
   *
   */
  def deleteAllTagsAsync(owner: Owner): Future[Unit]

  /**
   *
   */
  def deleteAllTagsAsync(name: TagName): Future[Unit]

  /**
   *
   */
  def replaceAllTagsAsync(owner: Owner, names: TagNameSet): Future[Unit] 
}
