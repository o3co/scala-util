package jp.o3co.tag.dictionary

import java.util.Date
import jp.o3co.datastore.KeyValueStoreProtocol

package object store {
  case class TermEntity(key: TagName, segment: TagSegment, label: TagLabel, created: Date = new Date(), updated: Date = new Date()) {
    def update(key: TagName = this.key, segment: TagSegment = this.segment, label: TagLabel = this.label) = {
      new TermEntity(key, segment, label, this.created, new Date())
    }
  }

  type TermPK = (TagName, TagSegment)

  trait Protocol extends KeyValueStoreProtocol[TermPK, TermEntity] {
    case class ContainsLabelKey(key: TagName, segment: Option[TagSegment] = None)
    case class ContainsLabelKeyComplete(exists: Boolean)
    case class ContainsLabelKeyFailure(cause: Throwable)

    case class GetLabel(key: TagName, segment: TagSegment)
    case class GetLabelComplete(label: Option[TagLabel])
    case class GetLabelFailure(cause: Throwable)

    case class GetLabelKeys(label: TagLabel, segment: TagSegment)
    case class GetLabelKeysComplete(keys: Traversable[TagName])
    case class GetLabelKeysFailure(cause: Throwable)

    case class PutLabel(key: TagName, segment: TagSegment, label: TagLabel)
    case class PutLabelComplete(prev: Option[TagLabel])
    case class PutLabelFailure(cause: Throwable)

    case class DeleteLabel(key: TagName, segment: TagSegment)
    case class DeleteLabelComplete(deleted: Option[TagLabel])
    case class DeleteLabelFailure(cause: Throwable)
  }
  object Protocol extends Protocol
}
