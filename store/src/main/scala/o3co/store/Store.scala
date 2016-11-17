package o3co.store

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

/**
 * Base trait of the store.
 * Store only provide countAsync which count existence on this aggregation store.
 */
trait Store {

  implicit def executionContext: ExecutionContext

  /**
   *
   */
  def countAsync(): Future[Long]
}


object Store {
  
  trait Access {
    this: Store =>
  }
}
