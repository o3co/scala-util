package o3co.tag
package store 

import o3co.conversion._
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

/**
 * MappedKeyTagStore is a TagStore with proxy pattern which convert key to underlying key to store. 
 *
 *
 * {{{
 *   val store: TagStore
 *
 *   val mappedStore = KeyMapper(store, Conversion({id => ID(id), id => id.value}))
 * }}}
 */
trait MappedKeyTagStore[KOUTER, KINNER, T <: Tag[T]] extends TagStore[KOUTER, T] {

  def underlying: TagStore[KINNER, T]

  implicit def keyMapper: ReversibleConversion[KOUTER, KINNER] 

  implicit def reverseKeyMapper: Conversion[KINNER, KOUTER]  = keyMapper.reverse

  implicit def executionContext: ExecutionContext

  /**
   *
   */
  def getTagsAsync(owner: KOUTER) =
    underlying.getTagsAsync(owner)

  /**
   *
   */
  def getOwnersAsync(tag: T) =
    underlying.getOwnersAsync(tag)
      .map { owners => owners.map(o => o: KOUTER) }

  /**
   *
   */
  def tagExistsAsync(owner: KOUTER, tag: T) =
    underlying.tagExistsAsync(owner, tag)

  /**
   *
   */
  def putTagAsync(owner: KOUTER, tag: T) =
    underlying.putTagAsync(owner, tag)

  /**
   *
   */
  def putTagAsync(owner: KOUTER, tags: Set[T]) = 
    underlying.putTagAsync(owner, tags)

  /**
   *
   */
  def putTagSetAsync(tags: Set[(KOUTER, T)]) =
    underlying.putTagSetAsync(tags.map(t => (t._1: KINNER, t._2)))

  /**
   *
   */
  def deleteTagAsync(owner: KOUTER, tag: T) =
    underlying.deleteTagAsync(owner, tag)

  /**
   *
   */
  def deleteTagAsync(owner: KOUTER, tags: Set[T]) =
    underlying.deleteTagAsync(owner, tags)

  /**
   *
   */
  def deleteTagSetAsync(tags: Set[(KOUTER, T)]) =
    underlying.deleteTagSetAsync(tags.map(t => (t._1: KINNER, t._2)))

  /**
   *
   */
  def deleteAllTagsAsync(owner: KOUTER) =
    underlying.deleteAllTagsAsync(owner)

  /**
   *
   */
  def deleteAllTagsAsync(tag: T) =
    underlying.deleteAllTagsAsync(tag)

  /**
   *
   */
  def replaceAllTagsAsync(owner: KOUTER, tags: Set[T]) =
    underlying.replaceAllTagsAsync(owner, tags)

  /**
   */
  def replaceTagsAsync(owner: KOUTER, newTags: Set[T], oldTags: Set[T]) =
    underlying.replaceTagsAsync(owner, newTags, oldTags)
}
