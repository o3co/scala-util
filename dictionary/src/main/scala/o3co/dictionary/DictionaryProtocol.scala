package o3co.dictionary

trait BaseDictionaryProtocolLike[K, V] {
  case class ContainsKey(key: K)
  case class ContainsKeySuccess(exists: Boolean)
  case class ContainsKeyFailure(cause: Throwable)

  case class ContainsValue(value: V)
  case class ContainsValueSuccess(exists: Boolean)
  case class ContainsValueFailure(cause: Throwable)

  case class Get(key: K)
  case class GetSuccess(term: Option[V])
  case class GetFailure(cause: Throwable)

  case class Put(key: K, term: V)
  case class PutSuccess()
  case class PutFailure(cause: Throwable)

  case class Remove(key: K)
  case class RemoveSuccess()
  case class RemoveFailure(cause: Throwable)

  case class Keys()
  case class KeysSuccess(keys: Set[K])
  case class KeysFailure(cause: Throwable)

  case class Values()
  case class ValuesSuccess(values: Set[V])
  case class ValuesFailure(cause: Throwable)

}

trait BaseDictionaryProtocol[K, V] extends BaseDictionaryProtocolLike[K, V]

trait DictionaryProtocolLike[K, V] {

  /**
   * Find keys for value
   */
  case class FindKeys(value: V)
  case class FindKeysSuccess(keys: Set[K])
  case class FindKeysFailure(cause: Throwable)
}

trait DictionaryProtocol[K, V] extends BaseDictionaryProtocol[K, V] with DictionaryProtocolLike[K, V]

trait BiDictionaryProtocolLike[K, V] {

}

trait BiDictionaryProtocol[K, V] extends BaseDictionaryProtocol[K, V] with BiDictionaryProtocolLike[K, V]
