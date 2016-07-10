package jp.o3co.config

import com.typesafe.config.Config
import scala.concurrent.duration.FiniteDuration
import java.util.Locale

/**
 *
 */
trait Settings extends Implicits with ConfigExtensions {

  def config: Config

  lazy val timeout: FiniteDuration = config.getOrElse[FiniteDuration]("timeout", GlobalSettings.defaultTimeout)

  lazy val locale: Locale = config.getOrElse[Locale]("locale", GlobalSettings.defaultLocale)

  def getConfig(path: String): Config = config.getConfig(path)
}

object Settings {
  def apply(c: Config) = new Settings {
    val config = c
  }
}
