package o3co.tag

trait TagConverter[A <: Tag[A], B <: Tag[B]] extends TagSetTransformer[A, B] 
{

  def apply(tag: A): Option[B] =
    convert(tag)

  def convert(tag: A): Option[B]

  def transform(tags: Traversable[A]) = 
    tags
      .map(convert(_))
      .collect {
        case Some(t) => t
      }
      .toSet
}

object TagConverter {
  
  def apply[A <: Tag[A], B <: Tag[B]](f: A => Option[B]) = 
    new TagConverter[A, B] {
      def convert(tag: A) = f(tag)
    }
}

