package o3co.config

import scala.concurrent.duration.FiniteDuration
import com.typesafe.config.Config

/**
 * Common service settings
 */
trait BaseServiceSettingsLike extends SettingsLike {
  this: Settings => 
  
  /**
   * Get timeout 
   */
  def timeout: FiniteDuration = config.getOrElse("tiemout", GlobalSettings.defaults.timeout) 
}

trait ServiceSettingsLike extends BaseServiceSettingsLike {
  this: Settings =>

  def serviceClass: Option[Class[_]] = config.getOption[String]("class").map(clazz => Class.forName(clazz))
}

trait ServiceSettings extends Settings with ServiceSettingsLike

object ServiceSettings {
  def apply(c: Config) = 
    new Settings with ServiceSettings {
      val config = c
    }
}

