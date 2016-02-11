package jp.o3co.config 

import scala.reflect.runtime.universe._
import com.typesafe.config.Config
import java.util.Locale
import scala.concurrent.duration.FiniteDuration

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import scala.language.implicitConversions
/**
 *
 */
trait Helpers {
  /**
   *
   */
  implicit class ScalaLikeConfig(override val config: Config) extends ExtendedConfig 
}

object Helpers extends Helpers
