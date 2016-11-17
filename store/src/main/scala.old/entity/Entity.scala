package o3co.store.entity

/**
 */
trait Indexed[Key] {
  def id: Key
}

/**
 */
trait Entity[Key] extends Indexed[Key]
