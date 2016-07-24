package o3co.store.vs 

import o3co.store
import o3co.conversion._
import scala.concurrent.ExecutionContext

/**
 *
 */
trait ReadAccessMapping[To, From] extends ReadAccessLike[To] {
  this: ReadAccess[To] =>

  def endpoint: ReadAccess[From] 

  implicit def valueConverter: ReversibleConversion[From, To]

  implicit def reverseValueConverter = valueConverter.reverse

  implicit def executionContext: ExecutionContext

  def existsAsync(value: To) = endpoint.existsAsync(value: From)

  def valuesAsync() = endpoint.valuesAsync().map { values => 
    values.map(v => v: To)
  }
}

trait WriteAccessMapping[To, From] extends WriteAccessLike[To] {
  this: WriteAccess[To] =>

  def endpoint: WriteAccess[From] 

  implicit def valueConverter: ReversibleConversion[From, To]

  implicit def reverseValueConverter = valueConverter.reverse
  
  def putAsync(values: To *) = endpoint.putAsync(values.map(v => v: From): _*)

  def addAsync(values: To *) = endpoint.addAsync(values.map(v => v: From): _*)

  def deleteAsync(values: To *) = endpoint.deleteAsync(values.map(v => v: From): _*)
}

/**
 * ReadOnlyValueStoreMapping[To, From] is a ValueStore[To] with restrict READ-ONLY.
 * the endpoint of this value store is a ReadAccess[From].
 */
trait ReadOnlyValueStoreMapping[To, From] extends store.ProxyStore with ReadOnlyValueStore[To] with ReadAccessMapping[To, From] {
  def endpoint: store.Store with ReadAccess[From]
}

trait ValueStoreMapping[To, From] extends store.ProxyStore with ValueStore[To] with ReadAccessMapping[To, From] with WriteAccessMapping[To, From] {
  def endpoint: store.Store with ReadAccess[From] with WriteAccess[From]

  override def reverseValueConverter = valueConverter.reverse
}
