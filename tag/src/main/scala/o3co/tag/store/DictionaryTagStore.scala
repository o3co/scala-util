package o3co.tag
package store

import o3co.conversion._
import o3co.dictionary._
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.ExecutionContext

/**
 * Use dictionary to map tags
 */
trait DictionaryTagStore[OWNER, TNAME <: Tag[TNAME], TLABEL <: Tag[TLABEL]] extends MappedValueTagStore[OWNER, TNAME, TLABEL] {

  def underlying: TagStore[OWNER, TNAME]
 
  def dictionary: BiDictionary[TNAME, TLABEL]

  lazy val tagMapper = 
    Conversion({ name: TNAME => 
        dictionary.get(name).get
      }, { label: TLABEL => 
        dictionary.getKey(label).get
      })
}

/**
 *
 *
 * {{{
 *   val store: TagStore 
 *   val userTagDictionary: BiDictionary[TNAME, TLABEL]
 *
 *   DictionaryTagStore(store, dictionary)
 * }}}
 *
 */
object DictionaryTagStore {

  /**
   *
   */
  def apply[OWNER, TNAME <: Tag[TNAME], TLABEL <: Tag[TLABEL]](store: TagStore[OWNER, TNAME], dict: BiDictionary[TNAME, TLABEL])(implicit ec: ExecutionContext): DictionaryTagStore[OWNER, TNAME, TLABEL] = 
    new DictionaryTagStore[OWNER, TNAME, TLABEL] {
      val executionContext = ec
      val dictionary = dict
      val underlying = store
    }

  /**
   *
   */
  def apply[OWNER, TNAME <: Tag[TNAME], TLABEL <: Tag[TLABEL]](store: TagStore[OWNER, TNAME], dict: AsyncBiDictionary[TNAME, TLABEL])(implicit ec: ExecutionContext, timeout: FiniteDuration): DictionaryTagStore[OWNER, TNAME, TLABEL] = 
    new DictionaryTagStore[OWNER, TNAME, TLABEL] {
      val executionContext = ec 
      // Convert AsyncBiDictionary to BiDictionary
      val dictionary = BiDictionary[TNAME, TLABEL](dict)
      val underlying = store
    }
}
