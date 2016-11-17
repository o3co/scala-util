package o3co.store.kvs 

import o3co.store
import o3co.conversion._
import scala.concurrent.ExecutionContext

trait MappedReadAccessLike[K, A, B] extends ReadAccessLike[K, B] {
  this: ReadAccess[K, B] =>

  def endpoint: ReadAccess[K, A]

  def converter: Conversion[A, B]

  def existsAsync(key: K) = endpoint.existsAsync(key)

  def keysAsync = endpoint.keysAsync

  def getAsync(keys: K *) = endpoint.getAsync(keys: _*).map { values => 
    values.map(converter(_))
  }
  
  def getAsync(keys: Set[K] = Set()) = endpoint.getAsync(keys).map { kvs => 
    kvs.map {
      case (k, v) => (k, converter(v))
    }
  }
}

trait MappedWriteAccessLike[K, A, B] extends WriteAccessLike[K, B] {
  this: WriteAccess[K, B] =>

  // 
  def endpoint: WriteAccess[K, A] 

  def converter: ReversibleConversion[A, B]

  /**
   */
  def setAsync(kvs: Map[K, B]) = endpoint.setAsync(kvs.map {
    case (k, v) => (k, converter.reverse(v)) 
  })

  /**
   */
  def addAsync(kvs: Map[K, B]) = endpoint.addAsync(kvs.map {
    case (k, v) => (k, converter.reverse(v)) 
  })

//  def replaceAsync(key: K, value: V) = endpoint.replaceAsync(key: K, value: V)

  def deleteAsync(key: K *) = endpoint.deleteAsync(key: _*)

//  def deleteByKeyAsync(keys: K *) = endpoint.deleteByKeyAsync(keys: K *)

}

trait ReadOnlyMappedKeyValueStore[K, A, B] extends store.ProxyStore with ReadOnlyKeyValueStore[K, B] with MappedReadAccessLike[K, A, B] {
  def endpoint: store.Store with ReadAccess[K, A]

  def converter: Conversion[A, B]
}

trait MappedKeyValueStore[K, A, B] extends store.ProxyStore with KeyValueStore[K, B] with MappedReadAccessLike[K, A, B] with MappedWriteAccessLike[K, A, B] {
  
  def endpoint: store.Store with ReadAccess[K, A] with WriteAccess[K, A]

  def converter: ReversibleConversion[A, B]
}

