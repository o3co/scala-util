package o3co.store.vs 

import o3co.store.Store
import o3co.store.StoreLike

/**
 * ValueStore is a store type which is as a Set collection
 */
trait ReadOnlyValueStore[V] extends Store with ReadAccess[V]

trait ValueStore[V] extends ReadOnlyValueStore[V] with WriteAccess[V]

trait ReadOnlyValueStoreLike[V] extends StoreLike {
  this: ReadOnlyValueStore[V] =>

}

trait ValueStoreLike[V] extends StoreLike {
  this: ValueStore[V] => 
}

