package jp.o3co.tag
package filter

import jp.o3co.matcher.Matcher
import jp.o3co.matcher.Matchers

/**
 * Append prefix into label when filter TO names.
 * Filter and replace prefix when filter FROM names.
 */
case class PrefixTagFilter(prefix: String, removePrefix: Boolean = true) extends TagFilter[Tag] {

  val matcher = Matchers.PrefixMatcher(prefix)

  def filter(values: TagSet) = values
    .map { value => 
      matcher(value.toString) match {
        case matcher.Matched(matched) => 
          if(removePrefix) Option(Tag(value.toString.replaceFirst(s"$matched", "")))
          else Option(value)
        case _ => None
      }
    }
    .collect {
      case Some(x) => x
    }

  /**
   * If removePrefix is true then return PrependTagFilter 
   * Otherwise, return self 
   */
  def reverse = 
    if(removePrefix) PrependTagFilter(prefix)
    else this
}

case class PrependTagFilter(prefix: String) extends ReverseTagFilter[Tag] {
  def filter(values: TagSet): TagSet = values
    .map(value => Tag(prefix + value))

  def reverse = PrefixTagFilter(prefix) 
}

