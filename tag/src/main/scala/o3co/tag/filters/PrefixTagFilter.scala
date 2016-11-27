package o3co.tag
package filters

import o3co.matcher.Matchers

trait PrefixTagFilter[T <: Tag[T]] extends TagFilter[T] {
 
  def prefix: String

  lazy val matcher = Matchers.PrefixMatcher(prefix)

  def filter(tag: T) = matcher(tag.name) match {
    case matcher.Matched(_) => true
    case _ => false
  }
}

object PrefixTagFilter {

  def apply[T <: Tag[T]](p: String): PrefixTagFilter[T] = 
    new PrefixTagFilter[T] {
      val prefix = p
    }
}
