package jp.o3co.tag
package store
package impl

import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.ExecutionContext

trait BaseTagStoreImplLike[Owner, +This <: BaseTagStoreImplLike[Owner, This] with BaseTagStoreImpl[Owner]] extends mutable.MapLike[Owner, Set[TagName], This] with TagStore[Owner] {

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
   *
   */
  def getTagsForAsync(owner: Owner) = {
    val names = ownerTags(owner)
    Future.successful(names) 
  }//: Future[TagNameSet]

  /**
   *
   */
  def getOwnersForAsync(name: TagName) = {
    val owners = ownerTags.collect { 
      case (owner, tags) if tags.contains(name) => owner
    }
    Future.successful(owners.toSet)
  }//: Future[Set[Owner]] 

  /**
   *
   */
  def tagExistsAsync(owner: Owner, name: TagName) = {
    val exists = ownerTags(owner).contains(name)
    Future.successful(exists)
  }//: Future[Boolean]

  /**
   *
   */
  def putTagAsync(owner: Owner, name: TagName) = {
    ownerTags = ownerTags + (owner -> (ownerTags.getOrElse(owner, Set()) + name) )
    Future.successful((): Unit)
  }//: Future[Unit] 

  /**
   *
   */
  def putTagSetAsync(tags: Set[(Owner, TagName)]) = {
    tags.foreach { 
      case (o, n) => ownerTags = ownerTags + (o -> (ownerTags.getOrElse(o, Set()) + n) )
    }
    Future.successful((): Unit)
  }//: Future[Unit] 

  /**
   *
   */
  def deleteTagAsync(owner: Owner, name: TagName) = {
    ownerTags = ownerTags + (owner -> (ownerTags.getOrElse(owner, Set()) - name) )
    Future.successful((): Unit)
  }//: Future[Unit]

  /**
   *
   */
  def deleteTagSetAsync(tags: Set[(Owner, TagName)]) = {
    tags.foreach { 
      case (o, n) => 
        ownerTags = ownerTags + (o -> (ownerTags.getOrElse(o, Set()) - n) )
    }
    Future.successful((): Unit)
  }//: Future[Unit]

  /**
   *
   */
  def deleteAllTagsAsync(owner: Owner) = {
    ownerTags = ownerTags.filter { 
      case (o, _) if o == owner => false
      case _ => true
    }
    Future.successful()
  }//: Future[Unit]

  /**
   *
   */
  def deleteAllTagsAsync(name: TagName) = {
    ownerTags = ownerTags.map {
      case (o, ns) => (o, ns.filter(n => n != name))
    }
    Future.successful((): Unit)
  }//: Future[Unit]

  /**
   *
   */
  def replaceAllTagsAsync(owner: Owner, names: TagNameSet) = {
    ownerTags = ownerTags + (owner -> names)
    Future.successful((): Unit)
  }//: Future[Unit] 
}

trait BaseTagStoreImpl[O] extends mutable.Map[O, Set[TagName]] with TagStore[O]

class TagStoreImpl[O](implicit val executionContext: ExecutionContext) extends BaseTagStoreImpl[O] with BaseTagStoreImplLike[O, TagStoreImpl[O]] {

  override def empty = new TagStoreImpl[O]
}

object TagStoreImpl {
  def apply[O](implicit ec: ExecutionContext) = new TagStoreImpl[O]
}
