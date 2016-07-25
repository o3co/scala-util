package o3co.tag
package store

import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.ExecutionContext

trait BaseTagStoreImplLike[O, T <: Tag[T], +This <: BaseTagStoreImplLike[O, T, This] with BaseTagStoreImpl[O, T]] extends mutable.MapLike[O, Set[T], This] with TagStore[O, T] {

  implicit def executionContext: ExecutionContext

  var ownerTags: Map[O, Set[T]] = Map()

  def get(owner: O): Option[Set[T]] = ownerTags.get(owner)

  def iterator = ownerTags.iterator

  def +=(kv: (O, Set[T])): this.type = {
    ownerTags = ownerTags + kv
    this
  }

  def -=(owner: O): this.type = {
    ownerTags = ownerTags - owner 
    this
  }

  /**
   *
   */
  def getTagsAsync(owner: O) = {
    val names = ownerTags(owner)
    Future.successful(names) 
  }//: Future[Set[T]]

  /**
   *
   */
  def getOwnersAsync(name: T) = {
    val owners = ownerTags.collect { 
      case (owner, tags) if tags.contains(name) => owner
    }
    Future.successful(owners.toSet)
  }//: Future[Set[O]] 

  /**
   *
   */
  def tagExistsAsync(owner: O, name: T) = {
    val exists = ownerTags(owner).contains(name)
    Future.successful(exists)
  }//: Future[Boolean]

  /**
   *
   */
  def putTagAsync(owner: O, name: T) = {
    ownerTags = ownerTags + (owner -> (ownerTags.getOrElse(owner, Set()) + name) )
    Future.successful((): Unit)
  }//: Future[Unit] 

  /**
   *
   */
  def putTagSetAsync(tags: Set[(O, T)]) = {
    tags.foreach { 
      case (o, n) => ownerTags = ownerTags + (o -> (ownerTags.getOrElse(o, Set()) + n) )
    }
    Future.successful((): Unit)
  }//: Future[Unit] 

  /**
   *
   */
  def deleteTagAsync(owner: O, name: T) = {
    ownerTags = ownerTags + (owner -> (ownerTags.getOrElse(owner, Set()) - name) )
    Future.successful((): Unit)
  }//: Future[Unit]

  /**
   *
   */
  def deleteTagSetAsync(tags: Set[(O, T)]) = {
    tags.foreach { 
      case (o, n) => 
        ownerTags = ownerTags + (o -> (ownerTags.getOrElse(o, Set()) - n) )
    }
    Future.successful((): Unit)
  }//: Future[Unit]

  /**
   *
   */
  def deleteAllTagsAsync(owner: O) = {
    ownerTags = ownerTags.filter { 
      case (o, _) if o == owner => false
      case _ => true
    }
    Future.successful((): Unit)
  }//: Future[Unit]

  /**
   *
   */
  def deleteAllTagsAsync(name: T) = {
    ownerTags = ownerTags.map {
      case (o, ns) => (o, ns.filter(n => n != name))
    }
    Future.successful((): Unit)
  }//: Future[Unit]

  /**
   *
   */
  def replaceAllTagsAsync(owner: O, names: Set[T]) = {
    ownerTags = ownerTags + (owner -> names)
    Future.successful((): Unit)
  }//: Future[Unit] 
}

trait BaseTagStoreImpl[O, T <: Tag[T]] extends mutable.Map[O, Set[T]] with TagStore[O, T]

class TagStoreImpl[O, T <: Tag[T]](implicit val executionContext: ExecutionContext) extends BaseTagStoreImpl[O, T] with BaseTagStoreImplLike[O, T, TagStoreImpl[O, T]] {

  override def empty = new TagStoreImpl[O, T]
}

object TagStoreImpl {
  def apply[O, T <: Tag[T]](implicit ec: ExecutionContext) = new TagStoreImpl[O, T]
}
