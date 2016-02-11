package jp.o3co.dictionary
package i18n

import java.util.Locale

import multi.MultiDictionary

/**
 * MultiDictionary with Locale as a Key
 */
trait I18nDictionary[K, V] extends MultiDictionary[Locale, K, V] {

  def localeSet: Set[Locale]

  def locales: Iterable[Locale]
}

