package jp.o3co.config 

import com.typesafe.config.Config
import com.typesafe.config.ConfigValueType
import com.typesafe.config.{ConfigExtensions => BaseConfigExtensions}
import jp.o3co.locale.Locale
import scala.reflect.runtime.universe._
//import scala.collection.JavaConversions._
//import scala.collection.JavaConverters._
//import scala.language.implicitConversions

/**
 * Implicit extension
 */
trait ConfigExtensions extends BaseConfigExtensions {
  def config: Config

  def getLocale(path: String): Locale = Locale(config.getString("locale"))

  /**
   * Get config value as Config.
   * If config on path is a Config, then return 
   * Otherwise put ConfigValue into a new Config with innerPath.
   *
   * {{{
   *   // For config
   *   // {
   *   //   something = "path"
   *   // }
   *   // Or
   *   // {
   *   //   something {
   *   //      endpoint = "path"
   *   //   }
   *   // }
   *   config.getAsConfig("somthing", "endpoint")
   *   // return 
   *   // {
   *   //   endpoint = "path" 
   *   // }
   * }}}
   *
   * @param path Path to get
   * @param innerPath Path to store
   */
  def getAsConfig(path: String, innerPath: String): Config = {
    val value = config.getValue(path)
    value.valueType match {
      case ConfigValueType.OBJECT => config.getConfig(path)
      case _ =>  value.atPath(innerPath)
    }
  }
  override def get[T: WeakTypeTag](path: String): T = {
    (weakTypeOf[T] match {
      case t if t =:= typeOf[Locale]         => getLocale(path)
      case t => super.get[T](path)
    }).asInstanceOf[T]
  }
}

