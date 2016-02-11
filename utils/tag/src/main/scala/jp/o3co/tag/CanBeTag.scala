package jp.o3co.tag

/**
 *
 */
trait CanBeTag {
  /**
   * Trait to convert object to TagName 
   * This may be a shortcut for ReverseTagFilter
   *
   * {{{
   *   case class Content(id: String) extends CanBeTag {
   *     def toTag = TagName(this.id)
   *     // or 
   *     val ContentToTagFilter = ReverseTagFilter[Content]({content => TagName(content.id)})
   *     def toTag = ContentToTagFilter(this) 
   *   }
   * }}}
   */
  def toTag: TagName 
}
