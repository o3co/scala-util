package o3co.tag
package tags

/**
 * SlugTag is a Tag 
 */
case class SlugTag(name: String) extends Tag[SlugTag] {
  if(name.getBytes().length >= 256) {
    throw new IllegalArgumentException("SlugTag must be less than 256 bytes.")
  }

  if(!name.matches(SlugTag.Acceptable)) {
    throw new IllegalArgumentException(s"'$name' is not valid SlugTag pattern.")
  }

  def rename(name: String) = copy(name)
}

object SlugTag {
  val Acceptable = "[a-zA-Z0-9\\_\\.]+"
}
