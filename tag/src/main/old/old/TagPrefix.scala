package jp.o3co.tag

/**
 *
 */
case class TagPrefix(prefix: String) {
  /**
   *
   */
  override def toString: String = prefix

  /**
   * Create PrefixFilter for this Prefix
   *
   * {{{
   *   val prefix = TagPrefix("some")
   *   val tagFilter = prefix.toFilter
   * }}}
   */
  val toFilter = filter.PrefixTagFilter(prefix)

  /**
   * Create TagPrependFilter for this prefix
   * {{{
   *   val prefix = PrefixTag("some")
   *   val reverseFilter  = prefix.toReverseFilter
   *   // or 
   *   val reverseFilter = prefix.toFilter.reverse
   * }}}
   */
  val toReverseFilter = filter.PrependTagFilter(prefix)
}
