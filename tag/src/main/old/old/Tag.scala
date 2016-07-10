package jp.o3co.tag

/**
 *
 */
trait Tag {
  def toString: String

  /**
   * Convert Tag as TagName.
   * 
   * @throws IllegalArgumentException if name is illegal format
   */
  def asTagName: TagName = TagName(toString)

  /**
   * Convert Tag as TagLabel
   */
  def asTagLabel: TagLabel = TagLabel(toString)
}

/**
 *
 */
object Tag {
  
  /**
   * Default factory method of tag.
   *   
   */
  def apply(name: String): Tag = TagLabel(name)

  /**
   * Create TagName with name slug.
   */
  def slug(name: String): TagName = TagName.slug(name) 
}

/**
 * Common TagName.
 * TagName only accept alphanumeric chars and "_"(underscore), ":"(coron), and "."(dot)
 *
 * @param name Name of tag
 */
case class TagName(name: String) extends Tag {
 
  if(!name.matches(s"^[${TagName.ACCEPT_CHARS}]*$$")) {
    throw new IllegalArgumentException("Invalid char contains in TagKey.")
  }

  override def toString = name
}

/**
 *
 */
object TagName {
  val ACCEPT_CHARS = """A-Za-z0-9_:\."""

  /**
   * 
   */
  def slug(name: String) = 
    TagName(
      name
        .replace(" ", "_")
        .replaceAll(s"""[^${TagName.ACCEPT_CHARS}]""", "")
    )
}

/**
 * TagLabel is another Tag accept any chars.
 * This is not system friendly, but human friendly tag.
 * To convert TagLabel to TagName, use tag-dictionary.
 *
 * @param label Label of tag
 */
case class TagLabel(label: String) extends Tag {

  override def toString = label 
}

