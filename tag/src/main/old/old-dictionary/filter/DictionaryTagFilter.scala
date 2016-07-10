package jp.o3co.tag
package filter

import scala.concurrent.Await
import scala.concurrent.duration.FiniteDuration
import jp.o3co.tag.dictionary.TagDictionary
import jp.o3co.tag.dictionary.TagSegment
import jp.o3co.tag.filter.context.TagDictionarySupport

case class ToLabelTagDictionaryFilter(dictionary: TagDictionary, segment: Option[TagSegment] = None)(implicit timeout: FiniteDuration) extends TagFilter[TagLabel] {

  def filter(tags: TagSet) = {
    //val tagDictionaryContext = context.asInstanceOf[TagDictionarySupport]
    Await.result(dictionary.getLabelsFor(tags.map(_.asTagName), segment), timeout)
      .values
      .toSet
  }

  val reverse = ToNameTagDictionaryFilter(dictionary)
}

case class ToNameTagDictionaryFilter(dictionary: TagDictionary, segment: Option[TagSegment] = None)(implicit timeout: FiniteDuration) extends ReverseTagFilter[TagLabel] {
  def filter(tags: TagLabelSet) = {
    //val tagDictionaryContext = context.asInstanceOf[TagDictionarySupport]
    Await.result(dictionary.getNamesFor(tags.map(_.asTagLabel), segment), timeout)
      .values
      .toSet
  }

  val reverse = ToLabelTagDictionaryFilter(dictionary)
}

