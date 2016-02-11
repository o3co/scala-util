package jp.o3co.tag
package store
package impl

import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.ExecutionContext

trait BaseTagStoreImpl[O] extends mutable.Map[O, Set[TagName]] with TagStore[O]

trait BaseTagStoreImplLike[O, +This <: BaseTagStoreImplLike[O, This] with BaseTagStoreImpl[O]] extends mutable.MapLike[O, Set[TagName], This] with TagStore[O] {

  implicit def executionContext: ExecutionContext

  var ownerTags: Map[Owner, TagNameSet] = Map()

  def get(owner: Owner): Option[TagNameSet] = ownerTags.get(owner)

  def iterator = ownerTags.iterator

  def +=(kv: (Owner, TagNameSet)): this.type = {
    ownerTags = ownerTags + kv
    this
  }

  def -=(owner: Owner): this.type = {
    ownerTags = ownerTags - owner 
    this
  }

  /**
   * Check single tag is existed with owner
   */
  def containsTag(owner: Owner, tag: TagName) = {
    val exists = ownerTags(owner).contains(tag)
    Future(exists)
  }
 
  /**
   * Get all tags with owner
   */
  def getTags(owner: Owner) = {
    val tags = ownerTags.getOrElse(owner, Set())

    Future(tags)
  }
  
  /**
   * Add tags to owner
   */
  def addTags(owner: Owner, tags: TagNameSet) = {
    val newTags = ownerTags.getOrElse(owner, Set()) ++ tags
    ownerTags = ownerTags + (owner -> newTags)
    Future(newTags)
  }

  /**
   * Remove tags with owner
   */
  def removeTags(owner: Owner, tags: TagNameSet) = {
    val newTags = ownerTags.getOrElse(owner, Set()) -- tags
    ownerTags = ownerTags + (owner -> newTags)
    Future(newTags)
  }

  /**
   * Remove all tags with owner
   */
  def removeAllTags(owner: Owner) = {
    val prev = ownerTags.getOrElse(owner, Set())
    ownerTags = ownerTags - owner
    Future(prev)
  }

  def replaceTags(owner: Owner, tags: TagNameSet) = {
    val prev = ownerTags.getOrElse(owner, Set())
    ownerTags = ownerTags + (owner -> tags)
    Future(prev)
  }
}

class TagStoreImpl[O](implicit val executionContext: ExecutionContext) extends BaseTagStoreImpl[O] with BaseTagStoreImplLike[O, TagStoreImpl[O]] {

  override def empty = new TagStoreImpl[O]
}

object TagStoreImpl {
  def apply[O](implicit ec: ExecutionContext) = new TagStoreImpl[O]
}
