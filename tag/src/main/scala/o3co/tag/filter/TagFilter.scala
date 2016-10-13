package o3co.tag
package filter

/**
 *
 */
trait TagFilter[T <: Tag[T]] extends Function[T, Option[T]] {

  /**
   * Get reverse filter
   */
  def reverse: TagFilter[T]

  /**
   * Alias of filter 
   */
  def apply(tag: T): Option[T] = filter(tag)

  /**
   * Alias of filter
   */
  def apply(tags: Set[T]): Set[T] = filter(tags)

  /**
   * apply filter to tag
   */
  def filter(tag: T): Option[T]

  /**
   * Apply filter to tags
   */
  def filter(tags: Set[T]): Set[T] = 
    tags
      .map(tag => filter(tag))
      .collect{ 
        case Some(t) => t 
      }
}
