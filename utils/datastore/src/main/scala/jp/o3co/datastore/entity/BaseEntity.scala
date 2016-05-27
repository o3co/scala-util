package jp.o3co.datastore
package entity

trait BaseEntity[K] {
  type EntityKey = K

  def entityKey: EntityKey
}

abstract class AbstractBaseEntity[K](override val entityKey: K) extends BaseEntity[K]

