package jp.o3co.config 

import com.typesafe.config.Config
import com.typesafe.config.ConfigValueType
import jp.o3co.locale.Locale
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import scala.concurrent.duration._
import scala.language.implicitConversions
import scala.reflect.runtime.universe._

/**
 * Extension Methods for Config
 */
trait ConfigExtensionMethods {
  def config: Config

  def getFiniteDuration(path: String): FiniteDuration = 
    FiniteDuration(config.getDuration(path, MILLISECONDS), MILLISECONDS)

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
}


/**
 * Implicit extension
 */
trait ConfigScalaLikeOperation {
  def config: Config

  def get[T: WeakTypeTag](path: String): T = {
    (weakTypeOf[T] match {
      case t if t =:= typeOf[Duration]       => Duration(config.getDuration(path, MILLISECONDS), MILLISECONDS)
      case t if t =:= typeOf[List[Boolean]]  => config.getBooleanList(path).asScala.toList
      case t if t =:= typeOf[List[Number]]   => config.getNumberList(path).asScala.toList
      case t if t =:= typeOf[List[Int]]      => config.getIntList(path).asScala.toList
      case t if t =:= typeOf[List[Long]]     => config.getLongList(path).asScala.toList
      case t if t =:= typeOf[List[Double]]   => config.getDoubleList(path).asScala.toList
      case t if t =:= typeOf[List[String]]   => config.getStringList(path).asScala.toList
      case t if t =:= typeOf[String]         => config.getString(path)
      case t if t =:= typeOf[Number]         => config.getNumber(path)
      case t if t =:= typeOf[Int]      => config.getInt(path)
      case t if t =:= typeOf[Long]     => config.getLong(path)
      case t if t =:= typeOf[Double]   => config.getDouble(path)
      case t if t =:= typeOf[Boolean]  => config.getBoolean(path)
      case t => 
        // Try to cast the string value from 
        throw new RuntimeException(s"""Type "${t}" is not supported to get.""")
    })
      .asInstanceOf[T]
  }

  def getOption[T: WeakTypeTag](path: String) = {
    if(config.hasPath(path)) Some(get[T](path))
    else None
  }

  /**
   * {{{
   *   config.getOrElse("path", default)
   *   // or 
   *   config.getOrElse("path", {
   *     default
   *   })
   * }}}
   */
  def getOrElse[T: WeakTypeTag](path: String, default: => T): T = 
    getOption[T](path).getOrElse(default)
}

trait ExtendedConfig extends ConfigScalaLikeOperation with ConfigExtensionMethods {
  override def get[T: WeakTypeTag](path: String): T = {
    (weakTypeOf[T] match {
      case t if t =:= typeOf[Locale]         => getLocale(path)
      case t if t =:= typeOf[FiniteDuration] => getFiniteDuration(path)
      case t => super.get[T](path)
    }).asInstanceOf[T]
  }
}
