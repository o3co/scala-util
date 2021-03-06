package o3co.store.vs 

import o3co.store.StoreProtocol

trait ValueStoreProtocolLike[V] extends ValueStoreProtocol.Base 
  with ValueStoreProtocol.Read[V] 
  with ValueStoreProtocol.Write[V] 
//{
//  case class ValueExists(value: V)
//  case class ValueExistsSuccess(exists: Boolean)
//  case class ValueExistsFailure(cause: Throwable)
//
//  case object GetValues
//  case class  GetValuesSuccess(values: Seq[V])
//  case class  GetValuesFailure(cause: Throwable)
//
//  /**
//   * Upsert the value
//   */
//  case class  PutValues(value: Seq[V])
//  case object PutValuesSuccess
//  case class  PutValuesFailure(cause: Throwable)
//
//  case class  AddValues(values: Seq[V])
//  case object AddValuesSuccess
//  case class  AddValuesFailure(cause: Throwable)
//
//  case class  DeleteValues(value: Seq[V])
//  case object DeleteValuesSuccess
//  case class  DeleteValuesFailure(cause: Throwable)
//}

trait ValueStoreProtocol[V] extends StoreProtocol with ValueStoreProtocolLike[V]


object ValueStoreProtocol {

  /**
   */
  trait Base extends StoreProtocol

  /**
   *
   */
  trait Read[V] {
    this: Base =>

    case class ValueExists(value: V)
    case class ValueExistsSuccess(exists: Boolean)
    case class ValueExistsFailure(cause: Throwable)

    case object GetValues
    case class  GetValuesSuccess(values: Seq[V])
    case class  GetValuesFailure(cause: Throwable)
  }

  /**
   *
   */
  trait Write[V] {
    this: Base =>

    case class  PutValues(value: Seq[V])
    case object PutValuesSuccess
    case class  PutValuesFailure(cause: Throwable)

    case class  AddValues(values: Seq[V])
    case object AddValuesSuccess
    case class  AddValuesFailure(cause: Throwable)

    case class  DeleteValues(value: Seq[V])
    case object DeleteValuesSuccess
    case class  DeleteValuesFailure(cause: Throwable)
  }
}
