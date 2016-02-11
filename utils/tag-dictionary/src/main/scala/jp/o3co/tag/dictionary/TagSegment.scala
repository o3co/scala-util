package jp.o3co.tag.dictionary

import jp.o3co.locale.Locale

trait TagSegment {
  def toString: String
}

/**
 * TagSegment for i18n_support
 */
case class LocaleTagSegment(locale: Locale) extends TagSegment {
  override def toString = locale.toString
}

/**
 * Default TagSegment.
 */
case class NamedTagSegment(name: String) extends TagSegment{
  override def toString = name
}


trait ImplicitConverters {
  
  /**
   * Mixin extentional method into TagSegment
   */
  implicit class TagSegmentConverter(underlying: TagSegment) {
    
    def asLocaleTagSegment: LocaleTagSegment = LocaleTagSegment(Locale(underlying.toString))

    def asNamedTagSegment: NamedTagSegment = NamedTagSegment(underlying.toString)
  }
}

trait ImplicitConversions {
  implicit def tagSegmentToString(segment: TagSegment): String = segment.toString
}
