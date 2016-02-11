package jp.o3co.collection.mutable

import collection._
import collection.mutable.{Builder, MapBuilder}
import collection.generic.CanBuildFrom
  
object BiMap extends {
  
  def empty[A, B] = new BiMap[A, B]
      
  def apply[A, B](kvs: (A, B)*): BiMap[A, B] = {
    val m: BiMap[A, B] = empty
    for (kv <- kvs) m += kv
    m
  }
        
  def newBuilder[A, B]: Builder[(A, B), BiMap[A, B]] = 
    new MapBuilder[A, B, BiMap[A, B]](empty)
          
  implicit def canBuildFrom[A, B]: CanBuildFrom[BiMap[_, _], (A, B), BiMap[A, B]] = 
    new CanBuildFrom[BiMap[_, _], (A, B), BiMap[A, B]] {
      def apply(from: BiMap[_, _]) = newBuilder[A, B]
      def apply() = newBuilder[A, B]
    }
}

/**
 * FIXME: 
 *   BiMap should have two difference Maps, so indexed by both key, and value.
 *   Currently BiMap only has one immutable map. this can still 
 */
class BiMap[A, B] extends mutable.Map[A, B]
  with mutable.MapLike[A, B, BiMap[A, B]]
{
  protected var _values: immutable.Map[A, B] = immutable.Map()

  def get(key: A): Option[B] = _values.get(key)

  def getKey(value: B): Option[A] = _values.find(kv => kv._2 == value).map(_._1)

  def getKeyOrElse[A1 >: A](value: B, default: => A1): A1 = getKey(value) match {
      case Some(k) => k
      case None    => default
    }

  def getKeyOrElseUpdate(value: B, op: => A): A = {
    getKey(value) match {
      case Some(k) => k
      case None    => val k = op; this(k) = value; k
    }
  }

  def iterator: Iterator[(A, B)] = _values.iterator

  def +=(kv: (A, B)): this.type = {
    _values = (_values.filter { 
        case (k, v) if v == kv._2 => false
        case _ => true
      }) + kv

    this
  }

  def -=(key: A): this.type = {
    _values = _values.filter {
      case (k, v) if k == key => false
      case _ => true
    }
    this
  }
   
  override def empty: BiMap[A, B] = new BiMap[A, B]
}
