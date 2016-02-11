package jp.o3co.datastore
package entity

trait EntityStoreComponents[K, E <: BaseEntity[K]] {
  
  type EntityKey    = K

  type Entity = E
}
