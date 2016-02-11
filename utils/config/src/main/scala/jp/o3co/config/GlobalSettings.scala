package jp.o3co.config

import com.typesafe.config.{Config, ConfigFactory}
import jp.o3co.locale.Locale
import scala.concurrent.duration.FiniteDuration

object GlobalSettings extends Settings {

  /**
   *
   */
  lazy val config: Config = ConfigFactory.load()
    .withFallback(ConfigFactory.defaultReference())

  def defaultTimeout: FiniteDuration = config.getFiniteDuration("defaults.timeout")

  def defaultLocale: Locale = Locale(config.getString("defaults.locale"))
}
