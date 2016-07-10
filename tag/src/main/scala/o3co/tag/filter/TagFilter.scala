package o3co.tag
package filter

trait TagFilter[T1 <: Tag[T1], T2 <: Tag[T2]] extends Function[T1, Option[T2]] {
  /**
   * Get reverse filter
   */
  def reverse: TagFilter[T2, T1]

  def apply(tag: T1): Option[T2]

  def apply(tags: Set[T1]): Set[T2] = 
    tags
      .map(tag => apply(tag))
      .collect{ case Some(t) => t }
}
