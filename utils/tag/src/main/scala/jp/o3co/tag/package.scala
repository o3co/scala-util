package jp.o3co

/**
 *
 */
package object tag {
  type TagNameSet  = Set[TagName]

  type TagLabelSet = Set[TagLabel]

  type TagSet      = Set[Tag]

  implicit def tagNameSetToTagSet(tags: TagNameSet): TagSet   = tags: TagSet

  implicit def tagLabelSetToTagSet(tags: TagLabelSet): TagSet = tags: TagSet

  implicit def tagNameSetToTagLabelSet(tags: TagNameSet): TagLabelSet = tags.map(tag => tag.asTagLabel)

  implicit def tagLabelSetToTagNameSet(tags: TagLabelSet): TagNameSet = tags.map(tag => tag.asTagName)

  implicit def tagSetToTagNameSet(tags: TagSet): TagNameSet = tags.map(tag => tag.asTagName)

  implicit def tagSetToTagLabelSet(tags: TagSet): TagLabelSet = tags.map(tag => tag.asTagLabel)
}
