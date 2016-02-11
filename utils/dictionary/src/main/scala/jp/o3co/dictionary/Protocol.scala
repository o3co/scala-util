package jp.o3co.dictionary

trait DictionaryProtocol[K, V] extends DictionaryComponents[K, V]{
  case class Contains(key: K)
  case class ContainsComplete(exists: Boolean)
  case class ContainsFailure(cause: Throwable)

  case class GetKeysOf(term: Term)
  case class GetKeysOfComplete(key: Iterable[TermKey])
  case class GetKeysOfFailure(cause: Throwable)

  case class GetTerm(key: TermKey)
  case class GetTermComplete(term: Option[Term])
  case class GetTermFailure(cause: Throwable)

  case class PutTerm(key: TermKey, term: Term)
  case class PutTermComplete(prev: Option[Term])
  case class PutTermFailure(cause: Throwable)

  case class DeleteTerm(key: TermKey)
  case class DeleteTermComplete(deleted: Option[Term])
  case class DeleteTermFailure(cause: Throwable)
}

trait WithDictionaryProtocol[K, V] {

  val protocol: DictionaryProtocol[K, V]
}
