package o3co.tag

/**
 */
trait Tag[T <: Tag[T]] {
  /**
   * Literal notation of the tag 
   */
  def name: String

  /**
   * Rename the tag
   */
  def rename(name: String): T
}
