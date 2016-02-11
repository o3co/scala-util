package jp.o3co.dictionary
package multi 

/**
 *
 */
trait MultiDictionaryProtocol[A, K, V] {

  case class Contains(key: K, selector: Option[A])
  case class ContainsComplete(exists: Boolean)
  case class ContainsFailure(cause: Throwable)

  case class GetKeysOf(term: V, selector: A)
  case class GetKeysOfComplete(key: Iterable[K])
  case class GetKeysOfFailure(cause: Throwable)

  case class GetTerm(key: K, selector: A)
  case class GetTermComplete(term: Option[V])
  case class GetTermFailure(cause: Throwable)

  case class PutTerm(key: K, term: V, selector: A)
  case class PutTermComplete(prev: Option[V])
  case class PutTermFailure(cause: Throwable)

  case class DeleteTerm(key: K, selector: A)
  case class DeleteTermComplete(deleted: Option[V])
  case class DeleteTermFailure(cause: Throwable)
}

trait WithMultiDictionaryProtocol[A, K, V] {

  val protocol: MultiDictionaryProtocol[A, K, V]
}

