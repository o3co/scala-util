package o3co.config

import com.typesafe.config.Config
import com.typesafe.config.ImplicitConfigExtensions

/**
 */
trait SettingsLike extends ImplicitConfigExtensions 

/**
 */
trait Settings extends SettingsLike {
  /**
   * Undeflying configuration
   */
  def config: Config

  def getConfig(path: String = null) = 
    Option(path) match  {
      case None    => config
      case Some(p) => config.getConfig(p)
    }

  def getConfigOption(path: String = null) = 
    Option(path) match  {
      case None    => Option(config)
      case Some(p) => config.getConfigOption(p)
    }

  def getConfigOrEmpty(path: String = null) = 
    Option(path) match  {
      case None    => config
      case Some(p) => config.getConfigOrEmpty(p)
    }

  def getAsConfig(path: String, inner: String) = 
    Option(path) match  {
      case None    => config
      case Some(p) => config.getAsConfig(p, inner)
    }
}

object Settings {
  def apply(c: Config) = new Settings { 
    val config = c
  }
}
