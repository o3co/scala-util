package o3co.dictionary
package impl

import scala.collection.mutable
import scala.concurrent.ExecutionContext

trait DictionaryImplLike[K, V] extends mutable.HashMap[K, V] {

  def findValue(value: V) = this.collect {
    case (k, v) if v == value => k
  }
}

class DictionaryImpl[K, V](implicit val executionContext: ExecutionContext) extends DictionaryImplLike[K, V] 
  with mutable.MapLike[K, V, DictionaryImpl[K, V]] 
{
  override def empty: DictionaryImpl[K, V] = new DictionaryImpl()
}

object DictionaryImpl {
  def apply[K, V](implicit ec: ExecutionContext) = new DictionaryImpl[K, V]

  def factory[K, V](implicit ec: ExecutionContext): () => DictionaryImpl[K, V] = {
    () => new DictionaryImpl[K, V]
  }
}

///**
// * DictionaryImpl is a single Dictionary Implementation which use HashMap for KeyValuePair
// *
// */
//trait BaseDictionaryImpl[K, V] extends mutable.HashMap[K, V] with Dictionary[K, V] with AsyncDictionaryLike[K, V] {
//
//  /**
//   * {@inheritDoc}
//   */
//  def getTerm(id: TermKey) = super.get(id)
//
//  /**
//   * {@inheritDoc}
//   */
//  def keysOf(term: Term) = super.collect {
//    case (key, value) if value == term => key
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  def putTerm(key: TermKey, term: Term) = super.put(key, term)
//
//  /**
//   * {@inheritDoc}
//   */
//  def deleteTerm(key: TermKey) = super.remove(key)
//
//  //override def empty: DictionaryImpl[K, V] = new DictionaryImpl[K, V]()
//  override def empty: DictionaryImpl[K, V] = throw new Exception("emp")
//}
//
//class DictionaryImpl[K, V](implicit val executionContext: ExecutionContext) extends BaseDictionaryImpl[K, V] with mutable.MapLike[K, V, DictionaryImpl[K, V]] {
//
//}
//
//object DictionaryImpl {
//  def apply[K, V](implicit ec: ExecutionContext) = new DictionaryImpl[K, V]
//
//  def factory[K, V](implicit ec: ExecutionContext): () => DictionaryImpl[K, V] = {
//    () => new DictionaryImpl[K, V]
//  }
//}
//
