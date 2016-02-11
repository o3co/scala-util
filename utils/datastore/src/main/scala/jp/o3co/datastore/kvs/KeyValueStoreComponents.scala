package jp.o3co.datastore
package kvs

trait KeyValueStoreComponents[K, V] {

  type Key = K

  type Value = V
}


