package o3co.config

import com.typesafe.config.Config 
import com.typesafe.config.ConfigFactory
import scala.collection.JavaConverters._

/**
 */
trait BaseApplicationSettings extends Settings {

  /**
   * Default configuration
   */
  def defaultConfig = GlobalSettings.config

  def defaults  = GlobalSettings.defaults

  /**
   * {{{
   *   val config: Config = parse(
   *    "test" -> Option(1)
   *   )
   *
   *   config.toString == "test = 1"
   * }}}
   *
   * {{{
   *   def config = parse(settings...).withFallback(defaultConfig)
   * }}}
   */
  def parse(values: (String, Option[Any]) *): Config = {
    ConfigFactory.parseMap(values.toMap.collect {
      case (k, Some(v)) => (k, v)
    }.toMap.asJava)
  }
}
