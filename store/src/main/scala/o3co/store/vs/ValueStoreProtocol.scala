package o3co.store
package vs 

trait ValueStoreProtocol[V] extends StoreProtocol
  with ValueStoreProtocol.Read[V] 
  with ValueStoreProtocol.Write[V] 


object ValueStoreProtocol {

  /**
   *
   */
  trait Read[V] {
    this: StoreProtocol =>

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
    this: StoreProtocol =>

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

  type Full[V] = Read[V] with Write[V]
}
