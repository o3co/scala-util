package jp.o3co.datastore
package entity

trait BaseEntity[K] {
  type EntityKey = K

  def entityKey: EntityKey
}

