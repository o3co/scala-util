package o3co.config

import com.typesafe.config.Config
import scala.concurrent.duration.FiniteDuration

/**
 *
 */
case class DefaultSettings(config: Config) extends Settings {

  /**
   * Get default timeout
   */
  def timeout: FiniteDuration = config.get[FiniteDuration]("timeout")
}
