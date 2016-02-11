package jp.o3co.config

import org.specs2.mutable.Specification
import java.util.Locale
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration._

class GlobalSettingsSpec extends Specification {
  
  "GlobalSettings" should {
    "provide defaults" in {
      
      GlobalSettings.defaultTimeout === FiniteDuration(1, SECONDS)
      GlobalSettings.defaultLocale === new Locale("en", "US")
    }
  }
}


