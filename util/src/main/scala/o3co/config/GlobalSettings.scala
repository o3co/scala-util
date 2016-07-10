package o3co.config

import com.typesafe.config.ConfigFactory

/**
 * GlobalSettings with ConfigFactory.
 * GlobalSettings use "application.conf" with "reference.conf" as a fallback
 * for its configuration
 */
object GlobalSettings extends Settings {

  val config = ConfigFactory.load().withFallback(ConfigFactory.defaultReference())

  /**
   * Get default configurations.
   *
   * {{{
   *  GlobalSettings.defaults.timeout
   * }}}
   */
  val defaults = DefaultSettings(config.getConfig("default"))
}
