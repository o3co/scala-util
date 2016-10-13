package o3co.tag
package filter
package filters

import o3co.matcher._

/**
 *
 */
case class PrefixTagFilter[T <: Tag[T]](prefix: String, removePrefix: Boolean = true) extends TagFilter[T] {

  /**
   *
   */
  lazy val matcher = Matchers.PrefixMatcher(prefix)

  /**
   *
   */
  def filter(from: T) = matcher(from.name) match {
    case matcher.Matched(matched) => 
      if(removePrefix) Option(from.rename(from.name.replaceFirst(matched.toString, "")))
      else Option(from)
    case _ => None
  }

  /**
   * 
   */
  def reverse = 
    if(removePrefix) PrependTagFilter[T](prefix)
    else this
}

case class PrependTagFilter[T <: Tag[T]](prefix: String) extends TagFilter[T] {

  def filter(from: T) = Option(from.rename(prefix + from.name))

  def reverse = PrefixTagFilter(prefix)
}
