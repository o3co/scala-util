package jp.o3co.tag

import java.util.Locale
import jp.o3co.dictionary.multi._

package object dictionary {

  trait Protocol extends MultiDictionaryProtocol[TagSegment, TagName, TagLabel] {

    case class ContainsKey(key: TagName, segment: Option[TagSegment] = None)
    case class ContainsKeyComplete(exists: Boolean)
    case class ContainsKeyFailure(cause: Throwable)

    case class GetNamesFor(label: Traversable[TagLabel], segment: Option[TagSegment] = None)
    case class GetNamesForComplete(names: Map[TagLabel, TagName])
    case class GetNamesForFailure(cause: Throwable)

    case class GetLabelsFor(name: Traversable[TagName], segment: Option[TagSegment] = None)
    case class GetLabelsForComplete(labels: Map[TagName, TagLabel])
    case class GetLabelsForFailure(cause: Throwable)
  }

  object Protocol extends Protocol
}
