package jp.o3co.tag

import scala.language.implicitConversions

/**
 *
 */
trait BaseImplicitConverters {

  implicit class StringAsTag(value: String) {
    def asTagLabel: TagLabel = TagLabel(value)

    def asTagName: TagName   = TagName.slug(value)
  }
}

trait ImplicitConverters extends BaseImplicitConverters
  with filter.ImplicitConverters

trait BaseImplicitConversions {

  /**
   * Implict conversion from Tag to String
   *
   * @param tag 
   * @return String
   */
  implicit def tagToString(tag: Tag): String = tag.toString

  /**
   * Implict conversion from String to Tag 
   *
   * @param name
   * @return TagLabel
   */
  implicit def stringToTag(name: String): TagLabel = TagLabel(name)

  /**
   *
   */
  implicit def tagPrefixToString(prefix: TagPrefix): String = prefix.toString
}

trait ImplicitConversions extends BaseImplicitConversions 
  with filter.ImplicitConversions


trait Implicits extends ImplicitConversions with ImplicitConverters
