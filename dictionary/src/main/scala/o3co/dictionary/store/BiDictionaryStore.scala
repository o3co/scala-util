package o3co.dictionary.store

import o3co.store.kvs.KeyValueStore
//import o3co.store.slick.SlickKeyValueStoreLike

trait BiDictionaryStore[K, V] extends KeyValueStore[K, V] {

}

//trait SlickBiDictionaryStoreLike[K, V] extends SlickKeyValueStoreLike[K, V] with BiDictionaryStore[K, V] {
//
//  import profile.api._
//
//  /**
//   *
//   */
//  abstract class DictionaryTable(tag: Tag, name: String) extends KeyValueTable(tag, name) {
//    // Add unique constrait into value
//    def idxUniqueValue = index("idx_unique_value", (value), unique = true)
//  }
//
//  def terms = keyValues
//}
//
//trait SlickBiDictionaryStore[K, V] extends SlickBiDictionaryStoreLike[K, V] {
//
//  import profile.api._
//
//  def tableName: String
//
//  class Terms(tag: Tag) extends DictionaryTable(tag, tableName)
//}
