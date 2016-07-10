package o3co.tag
package filter
package filters

import o3co.dictionary.BiDictionary
import o3co.tag.tags._

trait DictionaryFilter[T1 <: Tag[T1], T2 <: Tag[T2]] extends TagFilter[T1, T2] {
  root => 

  def dictionary: BiDictionary[T1, T2]

  def apply(from: T1) =
    dictionary.get(from)

  def apply(froms: Traversable[T1]) =
    froms.map(from => dictionary.get(from))

  lazy val reverse = ReverseDictionaryFilter(dictionary)

  case class ReverseDictionaryFilter(dictionary: BiDictionary[T1, T2]) extends TagFilter[T2, T1] {

    def apply(from: T2) = 
      dictionary.getKey(from)

    def apply(froms: Traversable[T2]) = 
      froms.map(from => dictionary.getKey(from))

    val reverse = root
  }
}

/**
 * Common pattern
 */
class SlugLabelDictionaryFilter(val dictionary: BiDictionary[SlugTag, LabelTag]) extends DictionaryFilter[SlugTag, LabelTag] {

}
