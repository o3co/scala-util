package jp.o3co.tag
package filter

trait ImplicitConverters {
  
  import ImplicitConversions._

  implicit class ReverseTagFilterAsTagNameFilter[T](filter: Filter[Set[T], TagSet]) {
    def toTagNameFilter: Filter[Set[T], TagNameSet] = filter.andThen({tags => tags.map(tag => tag.asTagName)})
  }
}
object ImplicitConverters extends ImplicitConverters

trait ImplicitConversions {
  import scala.language.implicitConversions

  implicit def reverseTagFilterToReverseTagNameFilter[T](filter: Filter[Set[T], TagSet]): Filter[Set[T], TagNameSet] = 
    filter.andThen({tags => tags.map(tag => tag.asTagName)})
}

object ImplicitConversions extends ImplicitConversions


trait Implicits extends ImplicitConversions with ImplicitConverters
object Implicits
