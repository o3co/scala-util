package o3co.tag

/**
 *
 */
trait TagSetTransformer[A <: Tag[A], B <: Tag[B]] {
  transformer =>

  def apply(tags: Traversable[A]): Set[B] = 
    transform(tags)
  
  /**
   * Transform tag
   */
  def transform(tags: Traversable[A]): Set[B]

  def compose[C <: Tag[C]](after: TagSetTransformer[B, C]): TagSetTransformer[A, C] = 
    TagSetTransformer[A, C](tags => after.transform(transformer.transform(tags)))

  /**
   * Compose alias
   */
  def ->[C <: Tag[C]](other: TagSetTransformer[B, C]): TagSetTransformer[A, C] = 
    compose(other)
}

/**
 *
 */
object TagSetTransformer {

  /**
   * Factory from function 
   */
  def apply[A <: Tag[A], B <: Tag[B]](f: Traversable[A] => Set[B]): TagSetTransformer[A, B] = 
    new TagSetTransformer[A, B] {
      def transform(tags: Traversable[A]) = f(tags)
    }
}
