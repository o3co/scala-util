package jp.o3co.dictionary
package i18n
package impl 

import org.specs2.mutable.Specification
import java.util.Locale
import scala.concurrent.ExecutionContext
import jp.o3co.dictionary.impl.DictionaryImpl

class I18nDictionaryImplSpec extends Specification {
  
  import ExecutionContext.Implicits

  "I18nDictionaryImpl" should {
    "ok" in {
      val dictionary = I18nDictionaryImpl[String, String]().withValueFactory{ locale => new DictionaryImpl[String, String] }

      val ja_JP: Locale = new Locale("ja", "JP")
      val en_US: Locale = new Locale("en", "US")

      false === dictionary.contains(ja_JP)
      false === dictionary.contains(en_US)

      dictionary.putDictionary(ja_JP, DictionaryImpl[String, String])
      //
      dictionary(ja_JP) must beAnInstanceOf[Dictionary[String, String]] 
      dictionary.size === 1

      // AutoExpandableMultiDictionaryLike create new Dictionary for the segment
      // Create by default
      //dictionary.dictionaries(en_US) must beAnInstanceOf[LocaledDictionary[String, String]]
      dictionary(en_US) must beAnInstanceOf[Dictionary[String, String]] 
      dictionary.size === 2
    }
  }
}
