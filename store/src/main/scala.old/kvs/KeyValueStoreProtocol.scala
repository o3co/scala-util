package o3co.store.kvs

import o3co.store.StoreProtocol

trait KeyValueStoreProtocolLike[K, V] {
  case class ExistsByKey(key: K)
  case class ExistsByKeySuccess(exists: Boolean)
  case class ExistsByKeyFailure(cause: Throwable)

  case object GetKeys
  case class  GetKeysSuccess(keys: Set[K])
  case class  GetKeysFailure(cause: Throwable)

  case class Get(key:K)
  case class GetSuccess(value: Option[V])
  case class GetFailure(cause: Throwable)

  case class GetByKeys(keys: Seq[K])
  case class GetByKeysSuccess(values: Seq[V])
  case class GetByKeysFailure(cause: Throwable)

  ///**
  // * Get values in order of keys.
  // * Some values might be missed when its not exists. 
  // */
  //case class GetByKeys(keys: Seq[K])
  //case class GetByKeysSuccess(values: Seq[V])
  //case class GetByKeysFailure(cause: Throwable)

  /**
   * Get KeyValue pairs.
   */
  case class GetKeyValues(keys: Set[K])
  case class GetKeyValuesSuccess(kvs: Map[K, V])
  case class GetKeyValuesFailure(cause: Throwable)

  /**
   * Upsert value for key
   */
  case class  SetKeyValues(kvs: Map[K, V])
  case object SetKeyValuesSuccess
  case class  SetKeyValuesFailure(cause: Throwable)

  /**
   * Insert value for key
   */
  case class  AddKeyValues(kvs: Map[K, V])
  case object AddKeyValuesSuccess
  case class  AddKeyValuesFailure(cause: Throwable)

//  /**
//   * Replace value for key.
//   */
//  case class  ReplaceKeyValue(key:K, value: V)
//  case object ReplaceKeyValueSuccess
//  case class  ReplaceKeyValueFailure(cause: Throwable)

  case class  DeleteByKeys(keys: Seq[K])
  case object DeleteByKeysSuccess
  case class  DeleteByKeysFailure(cause: Throwable)

  //case class  DeleteByKeys(keys: Seq[K])
  //case object DeleteByKeysSuccess
  //case class  DeleteByKeysFailure(cause: Throwable)
}

trait KeyValueStoreProtocol[K, V] extends StoreProtocol with KeyValueStoreProtocolLike[K, V]
