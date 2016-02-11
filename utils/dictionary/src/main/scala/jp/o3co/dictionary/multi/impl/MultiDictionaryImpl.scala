package jp.o3co.dictionary
package multi
package impl 

import scala.collection.mutable
import scala.collection.immutable

/**
 *
 */
trait MultiDictionaryImplLike[A, K, V, +This <: MultiDictionaryImplLike[A, K, V, This] with BaseMultiDictionaryImpl[A, K, V]] 
  extends mutable.MapLike[A, Dictionary[K, V], This] 
{
  var dictionaries: immutable.Map[A, Dictionary[K, V]] = immutable.Map[A, Dictionary[K, V]]()

  def get(key: A): Option[Dictionary[K, V]] = dictionaries.get(key)

  def iterator: Iterator[(A, Dictionary[K, V])] = dictionaries.iterator

  def +=(kv: (A, Dictionary[K, V])): this.type = {
    dictionaries = dictionaries + kv
    this
  }

  def -=(key: A): this.type = {
    dictionaries = dictionaries - key
    this
  }
}

/**
 *
 */
trait BaseMultiDictionaryImpl[A, K, V] extends mutable.Map[A, Dictionary[K, V]] with MultiDictionary[A, K, V] {

  def getDictionary(selector: A): Option[Dictionary[K, V]] = get(selector)

  def putDictionary(selector: A, newDictionary: Dictionary[K, V]) = put(selector, newDictionary) 

  def contains(key: K, selector: Option[A]) = selector match {
    case Some(s) => dictionary(s).contains(key)
    case None    => 
      (collectFirst {
        case (s, d) if d.contains(key) => true
      }).getOrElse(false)
  }

  def keysOf(term: V, selector: A): Iterable[K] = dictionary(selector).keysOf(term) 

  def getTerm(key: K, selector: A): Option[V]   = dictionary(selector).getTerm(key)

  def putTerm(key: K, term: V, selector: A): Option[V] = dictionary(selector).putTerm(key, term)

  def deleteTerm(key: K, selector: A): Option[V] = dictionary(selector).deleteTerm(key)

}

/**
 *
 */
class MultiDictionaryImpl[A, K, V] extends BaseMultiDictionaryImpl[A, K, V]
  //with MultiDictionaryImplLike[A, Dictionary[K, V], MultiDictionaryImpl[A, K, V]]
  with MultiDictionaryImplLike[A, K, V, MultiDictionaryImpl[A, K, V]]
{
  override def empty: MultiDictionaryImpl[A, K, V] = new MultiDictionaryImpl[A, K, V]
}

