package jp.o3co.tag
package dictionary

import scala.concurrent.Future

trait TagDictionaryStore {
  def containsLabelKey(key: TagName, segmet: Option[TagSegment]): Future[Boolean]
  def getLabel(key: TagName, segment: TagSegment): Future[Option[TagLabel]]
  def getLabelKeys(label: TagLabel, segment: TagSegment): Future[Traversable[TagName]]
  def putLabel(key: TagName, segment: TagSegment, label: TagLabel): Future[Option[TagLabel]]
  def deleteLabel(key: TagName, segment: TagSegment): Future[Option[TagLabel]]
}

