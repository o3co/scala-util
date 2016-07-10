package o3co.tag
package tags

/**
 *
 */
case class LabelTag(name: String) extends Tag[LabelTag] {
  if(name.getBytes().length >= 256) {
    throw new IllegalArgumentException("SlugTag must be less than 256 bytes.")
  }

  def rename(name: String) = copy(name)
}
