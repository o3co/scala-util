package jp.o3co.config 

import com.typesafe.config.Config

/**
 *
 */
trait Helpers {
  implicit class ScalaLikeConfig(override val config: Config) extends ConfigExtensionMethods 
}

object Helpers extends Helpers
