package jp.o3co.config 

import com.typesafe.config.Config
import scala.concurrent.duration._
import scala.reflect.runtime.universe._
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import scala.language.implicitConversions

/**
 * Implicit extension
 */
trait ConfigExtensionMethods {
  def config: Config

  def get[T: WeakTypeTag](path: String): T = {
    (weakTypeOf[T] match {
      case t if t =:= typeOf[FiniteDuration] => FiniteDuration(config.getDuration(path, MILLISECONDS), MILLISECONDS)
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
      case t => throw new RuntimeException(s"""Type "${t}" is not supported to get.""")
    })
      .asInstanceOf[T]
      //case t if t =:= typeOf[Float] => config.getFloat(path)
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

  def getFiniteDuration(path: String): FiniteDuration = 
    FiniteDuration(config.getDuration(path, MILLISECONDS), MILLISECONDS)
}
