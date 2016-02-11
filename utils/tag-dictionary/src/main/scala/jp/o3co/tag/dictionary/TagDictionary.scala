package jp.o3co.tag
package dictionary

import jp.o3co.dictionary.multi.AsyncMultiDictionary
import scala.concurrent.Future

/**
 *
 */
trait TagDictionary extends AsyncMultiDictionary[TagSegment, TagName, TagLabel] {

  /**
   *
   */
  def getNamesFor(labels: Traversable[TagLabel], segment: Option[TagSegment] = None): Future[Map[TagLabel, TagName]]

  /*
   *
   */
  def getLabelsFor(names: Traversable[TagName], segment: Option[TagSegment] = None): Future[Map[TagName, TagLabel]]
}

