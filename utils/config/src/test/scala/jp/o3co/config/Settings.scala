package jp.o3co.config

import org.specs2.mutable.Specification
import java.util.Locale
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration._

class SettingsSpec extends Specification {
  
  "Settings" should {
    "provide timeout and locale with config" in {
      val settings = new Settings {
        override val config = ConfigFactory.parseString(
"""
timeout = "2s"
locale  = "ja_JP"
"""
        )
      }
      settings.timeout === FiniteDuration(2, SECONDS)

      settings.locale === new Locale("ja", "JP")
    }
    "provide timeout and locale without config" in {
      val settings = new Settings {
        override val config = ConfigFactory.parseString(
"""
"""
        )
      }
      settings.timeout === GlobalSettings.defaultTimeout
      settings.locale === GlobalSettings.defaultLocale
    }
  }
}

