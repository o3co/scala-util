package o3co.tag
package filter
package filters

import o3co.matcher._

case class PrefixFilter[T <: Tag[T]](prefix: String, removePrefix: Boolean = true) extends TagFilter[T, T] {

  lazy val matcher = Matchers.PrefixMatcher(prefix)

  def apply(from: T) = matcher(from.name) match {
    case matcher.Matched(matched) => 
      if(removePrefix) Option(from.rename(from.name.replaceFirst(matched.toString, "")))
      else Option(from)
    case _ => None
  }

  def reverse = 
    if(removePrefix) PrependFilter[T](prefix)
    else this
}

case class PrependFilter[T <: Tag[T]](prefix: String) extends TagFilter[T, T] {

  def apply(from: T) = Option(from.rename(prefix + from.name))

  def reverse = PrefixFilter(prefix)
}
