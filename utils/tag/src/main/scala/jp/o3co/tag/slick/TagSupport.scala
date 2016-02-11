package jp.o3co.tag
package slick

import _root_.slick.driver.JdbcProfile

/**
 * To support tag on slick
 */
trait TagSupport {

  val profile: JdbcProfile
  import profile.api._

  /**
   * Implicit conversion to support TagName as a column type
   */
  implicit def tagNameColumnType  = MappedColumnType.base[TagName, String](t => t.toString, TagName(_))
}
