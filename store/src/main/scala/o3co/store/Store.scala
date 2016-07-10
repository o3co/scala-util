package o3co.store

import scala.concurrent.Future

/**
 * Base trait of the store.
 * Store only provide countAsync which count existence on this aggregation store.
 */
trait Store {
  /**
   *
   */
  def countAsync(): Future[Long]
}

trait StoreLike {
  this: Store =>

}

