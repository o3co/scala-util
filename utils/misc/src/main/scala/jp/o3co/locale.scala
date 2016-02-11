package jp.o3co

import org.apache.commons.lang3.LocaleUtils

package object locale {

  type Locale = java.util.Locale

  object Locale {
    def apply(locale: String): Locale = LocaleUtils.toLocale(locale) 
  }

  /**
   * Locale ImpllicitExtension 
   */
  trait ImplicitConversions {
    import scala.language.implicitConversions

    implicit def stringToLocale(locale: String): Locale = LocaleUtils.toLocale(locale)

    implicit def localeToString(locale: Locale): Locale = locale.toString
  }
  
  object ImplicitConversions extends ImplicitConversions
}
