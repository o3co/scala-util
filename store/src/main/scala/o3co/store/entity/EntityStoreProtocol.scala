package o3co.store.entity

import o3co.store.StoreProtocol
import o3co.store.vs.ValueStoreProtocol
import o3co.store.kvs.KeyValueStoreProtocol

trait EntityStoreProtocolLike[K, E <: Entity[K]] {
}

trait EntityStoreProtocol[K, E <: Entity[K]] extends ValueStoreProtocol[E] with KeyValueStoreProtocol[K, E] with EntityStoreProtocolLike[K, E] {

}
