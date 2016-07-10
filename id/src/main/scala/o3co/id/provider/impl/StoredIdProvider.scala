package o3co.id.provider

import o3co.generate.Generate
import o3co.store.vs.ValueStore
import scala.concurrent.ExecutionContext

/**
 * Implemetation with ValueStore 
 */
trait StoredIdProviderLike[ID] extends IdProviderLike[ID] {
  this: IdProvider[ID] => 

  /**
   * ID Generator
   */
  def generator: Generate[ID]

  def idStore: ValueStore[ID]

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
    val id = generator.generate.get
    idStore.putAsync(id).map(_ => id)
  }

  /**
   * Release ID from store
   */
  protected def doReleaseAsync(id: ID) = {
    idStore.deleteAsync(id)
  }
}

trait StoredIdProvider[ID] extends IdProvider[ID] with StoredIdProviderLike[ID]
