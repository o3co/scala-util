package jp.o3co.datastore
package kvs

trait KeyValueStoreProtocolLike[K, V] extends KeyValueStoreComponents[K, V] {
  case class Contains(key: Key)
  case class ContainsSuccess(exists: Boolean)
  case class ContainsFailure(cause: Throwable)

  case class Get(key: Key)
  case class GetSuccess(value: Option[Value])
  case class GetFailure(cause: Throwable)

  case class Put(key: Key, value: Value)
  case class PutSuccess(prev: Option[Value])
  case class PutFailure(cause: Throwable)

  case class Delete(key: Key)
  case class DeleteSuccess(deleted: Option[Value])
  case class DeleteFailure(cause: Throwable)
}
