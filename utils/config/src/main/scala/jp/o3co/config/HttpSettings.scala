package jp.o3co.config

import com.typesafe.config.Config

/**
 *
 */
trait HttpSettingsLike extends Settings {

  /**
   *
   */
  def host: String = config.getOrElse[String]("hostname", "localhost")

  /**
   *
   */
  def port: Int    = config.getOrElse[Int]("port", 8080)
}

/**
 *
 */
case class HttpSettings(config: Config) extends HttpSettingsLike
