package o3co.store.kvs 

import o3co.store
import o3co.conversion._
import scala.concurrent.ExecutionContext

trait ReadAccessMapping[ToKey, FromKey, ToValue, FromValue] extends ReadAccessLike[ToKey, ToValue] {
  this: ReadAccess[ToKey, ToValue] =>

  def endpoint: ReadAccess[FromKey, FromValue]

  implicit def keyConverter: ReversibleConversion[FromKey, ToKey]

  implicit def reverseKeyConverter = keyConverter.reverse

  implicit def valueConverter: Conversion[FromValue, ToValue]

  def existsAsync(key: ToKey) = endpoint.existsAsync(key: FromKey)

  def keysAsync = endpoint.keysAsync.map(_.map(k => k: ToKey))

  def getAsync(keys: ToKey *) = endpoint.getAsync(keys.map(k => k: FromKey): _*).map { values => 
    values.map(v => v: ToValue)
  }
  
  def getAsync(keys: Set[ToKey] = Set()) = endpoint.getAsync(keys.map(k => k: FromKey)).map { kvs => 
    kvs.map {
      case (k, v) => (k: ToKey, v: ToValue)
    }
  }
}

trait WriteAccessMapping[ToKey, FromKey, ToValue, FromValue] extends WriteAccessLike[ToKey, ToValue] {
  this: WriteAccess[ToKey, ToValue] =>

  // 
  def endpoint: WriteAccess[FromKey, FromValue] 

  implicit def keyConverter: ReversibleConversion[FromKey, ToKey]

  implicit def reverseKeyConverter = keyConverter.reverse

  implicit def valueConverter: ReversibleConversion[FromValue, ToValue]

  implicit def reverseValueConverter = valueConverter.reverse 

  /**
   */
  def setAsync(kvs: Map[ToKey, ToValue]) = endpoint.setAsync(kvs.map {
    case (k, v) => (k: FromKey, v: FromValue) 
  })

  /**
   */
  def addAsync(kvs: Map[ToKey, ToValue]) = endpoint.addAsync(kvs.map {
    case (k, v) => (k: FromKey, v: FromValue)
  })

//  def replaceAsync(key: ToKey, value: V) = endpoint.replaceAsync(key: ToKey, value: V)
  //def deleteAsync(keys: ToKey *) = endpoint.deleteAsync(keyConverter(keys).collect(case Success(k) => k): _*)
  def deleteAsync(keys: ToKey *) = endpoint.deleteAsync(keys.map(k => k: FromKey): _*)

//  def deleteByKeyAsync(keys: ToKey *) = endpoint.deleteByKeyAsync(keys: ToKey *)

}

trait ReadOnlyKeyValueStoreMapping[ToKey, FromKey, ToValue, FromValue] extends store.ProxyStore with ReadOnlyKeyValueStore[ToKey, ToValue] with ReadAccessMapping[ToKey, FromKey, ToValue, FromValue] {
  def endpoint: store.Store with ReadAccess[FromKey, FromValue]

}

trait KeyValueStoreMapping[ToKey, FromKey, ToValue, FromValue] extends store.ProxyStore with KeyValueStore[ToKey, ToValue] with ReadAccessMapping[ToKey, FromKey, ToValue, FromValue] with WriteAccessMapping[ToKey, FromKey, ToValue, FromValue] {
  
  def endpoint: store.Store with ReadAccess[FromKey, FromValue] with WriteAccess[FromKey, FromValue]

  override def reverseKeyConverter = keyConverter.reverse
}


