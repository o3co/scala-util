package o3co.tag
package store
package slick 

import _root_.slick.driver.JdbcProfile
import o3co.tag.tags._

trait TagSupport {

  val profile: JdbcProfile
  import profile.api._

  implicit def slugTagColumnType = MappedColumnType.base[SlugTag, String](t => t.name, SlugTag(_))

  implicit def labelTagColumnType = MappedColumnType.base[LabelTag, String](t => t.name, LabelTag(_))
}
