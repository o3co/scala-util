package o3co.tag
package filter

/**
 * 
 */
trait TagFilter[A <: Tag[A], B <: Tag[B]] extends Function[A, Option[B]] {

  /**
   * Get reverse filter
   */
  def reverse: TagFilter[B, A]

  /**
   * Alias of filter 
   *
   * {{{
   *   val tag: A
   *   val optTag: Option[B] = tagFilter(tag)
   * }}}
   */
  def apply(tag: A): Option[B] = filter(tag)

  /**
   * Alias of filter
   */
  def apply(tags: Traversable[A]): Set[B] = filter(tags)

  /**
   * apply filter to tag
   */
  def filter(tag: A): Option[B]

  /**
   * Apply filter to tags
   */
  def filter(tags: Traversable[A]): Set[B] = 
    tags
      .map(tag => filter(tag))
      .collect{ 
        case Some(t) => t 
      }
      .toSet
}
