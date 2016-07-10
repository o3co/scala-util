package jp.o3co.id.provider
package impl

import jp.o3co.generator.Generator
import jp.o3co.id.store.IDStore
import scala.concurrent.ExecutionContext

/**
 * Implemetation with IDStore and generator
 */
trait StoredIDProviderImpl[ID] extends IDProviderImpl[ID] {

  /**
   * ID Generator
   */
  def generator: Generator[ID]

  def idStore: IDStore[ID]

  implicit def executionContext: ExecutionContext

  /**
   * Check the id exists or not
   */
  protected def doExistsAsync(id: ID) = {
    idStore.existsAsync(id)
  }

  /**
   * Generate new ID and store it.
   */
  protected def doGenerateAsync() = {
    val id = generator.generate()
    idStore.putAsync(id).map(_ => id)
  }

  /**
   * Release ID from store
   */
  protected def doReleaseAsync(id: ID) = {
    idStore.deleteAsync(id)
  }
}


