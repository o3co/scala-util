package jp.o3co.tag.dictionary
package store

import jp.o3co.datastore.KeyValueStore

trait BaseTagDictionaryStore extends TagDictionaryStore with KeyValueStore[TermPK, TermEntity] {

}
