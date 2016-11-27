package o3co.tag
package converters

/**
 */
trait PrependTagConverter[A <: Tag[A]] extends TagConverter[A, A] {

  def prefix: String

  def convert(tag: A) = 
    Option(tag.rename(prefix + tag.name))
}

object PrependTagConverter {

  def apply[T <: Tag[T]](p: String) = 
    new PrependTagConverter[T] {
      forward => 

      val prefix = p
    }
}

import o3co.matcher.Matchers

trait RemovePrefixTagConverter[T <: Tag[T]] extends TagConverter[T, T] {

  def prefix: String

  def acceptOnlyWithPrefix: Boolean 

  lazy val matcher = Matchers.PrefixMatcher(prefix)

  def convert(tag: T) = 
    matcher(tag.name) match {
      case matcher.Matched(matched) => 
        Option(tag.rename(tag.name.replaceFirst(matched.toString, "")))
      case _ if acceptOnlyWithPrefix => None
      case _ => Some(tag)
    }
}

