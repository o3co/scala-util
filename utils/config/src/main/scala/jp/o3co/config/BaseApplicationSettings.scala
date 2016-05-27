package jp.o3co.config

import com.typesafe.config.Config 
import com.typesafe.config.ConfigFactory
import scala.collection.JavaConverters._

trait BaseApplicationSettings extends Settings {
  def config: Config

  def defaultConfig: Config = ConfigFactory.load().withFallback(ConfigFactory.defaultReference)

  def parseMap(values: Map[String, Option[Any]]): Config = {
    ConfigFactory.parseMap(values.collect {
      case (k, Some(v)) => (k, v)
    }.asJava)
  }
}
