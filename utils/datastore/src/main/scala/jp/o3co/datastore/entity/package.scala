package jp.o3co.datastore

import scala.concurrent.ExecutionContext

/**
 * Package of datastore.entity
 *
 */
package object entity {
  
  trait EntityStoreDefinition[K, V <: BaseEntity[K]] extends EntityStoreComponents[K, V] {
    type BaseEntity              = entity.BaseEntity[K]
    type EntityStore             = entity.EntityStore[K, V]
    type EntityStoreActorLike    = entity.EntityStoreActorLike[K, V]
    type EntityStoreAdapterLike  = entity.EntityStoreAdapterLike[K, V]
    type EntityStoreProtocolLike = entity.EntityStoreProtocolLike[K, V]

    import scala.language.implicitConversions
    implicit def entityAsKey(entity: BaseEntity): K = entity.entityKey

    type EntityStoreImpl         = impl.EntityStoreImpl[K, V]
    object EntityStoreImpl {
      def apply(implicit ec: ExecutionContext) = new impl.EntityStoreImpl[K, V]
    }
  }
}

