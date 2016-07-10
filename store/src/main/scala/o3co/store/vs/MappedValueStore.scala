package o3co.store.vs 

import o3co.store
import o3co.conversion._
import scala.concurrent.ExecutionContext

trait MappedReadAccessLike[A, B] extends ReadAccessLike[B] {
  this: ReadAccess[B] =>

  def endpoint: ReadAccess[A] 

  def converter: ReversibleConversion[A, B]

  implicit def executionContext: ExecutionContext

  def existsAsync(value: B) = endpoint.existsAsync(converter.reverse(value))

  def valuesAsync() = endpoint.valuesAsync().map { values => 
    values.map(converter(_))
  }
}

trait MappedWriteAccessLike[A, B] extends WriteAccessLike[B] {
  this: WriteAccess[B] =>

  def endpoint: WriteAccess[A] 

  def converter: ReversibleConversion[A, B]
  
  def putAsync(values: B *) = endpoint.putAsync(values.map(converter.reverse(_)): _*)

  def addAsync(values: B *) = endpoint.addAsync(values.map(converter.reverse(_)): _*)

  def deleteAsync(values: B *) = endpoint.deleteAsync(values.map(converter.reverse(_)): _*)
}


trait ReadOnlyMappedValueStore[A, B] extends store.ProxyStore with ReadOnlyValueStore[B] with MappedReadAccessLike[A, B] {
  def endpoint: store.Store with ReadAccess[A]
}

trait MappedValueStore[A, B] extends store.ProxyStore with ValueStore[B] with MappedReadAccessLike[A, B] with MappedWriteAccessLike[A, B] {
  def endpoint: store.Store with ReadAccess[A] with WriteAccess[A]
}

