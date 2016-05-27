package jp.o3co.config 

import com.typesafe.config.Config
/**
 *
 */
trait Implicits {
  /**
   *
   */
  implicit class ExtendedConfig(override val config: Config) extends ConfigExtensions 
}

object Implicits extends Implicits
