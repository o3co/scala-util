package jp.o3co.tag
package filter
package context

import jp.o3co.tag.dictionary.TagSegment
import jp.o3co.tag.dictionary.LocaleTagSegment
import jp.o3co.tag.dictionary.NamedTagSegment
import java.util.Locale

trait TagDictionarySupport {
  def segment: Option[TagSegment] = None
}

/**
 *
 */
trait I18nTagDictionarySupport extends TagDictionarySupport {
  def locale: Locale
}

case class DefaultTagDictionaryFilterContext() extends FilterContext with TagDictionarySupport {
}
case class DefaultI18nTagDictionaryFilterContext(locale: Locale) extends FilterContext with I18nTagDictionarySupport {
  override def segment = Option(LocaleTagSegment(locale))
}
