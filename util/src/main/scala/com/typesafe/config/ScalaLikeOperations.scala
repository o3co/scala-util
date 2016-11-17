package com.typesafe.config

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import scala.concurrent.duration._
import scala.language.implicitConversions
import scala.reflect.runtime.universe._

/**
 *
 */
trait ScalaLikeOperations extends AsConfig {

  def get[T: WeakTypeTag](path: String): T = {
    (weakTypeOf[T] match {
      case t if t =:= typeOf[Duration]       => Duration(underlying.getDuration(path, MILLISECONDS), MILLISECONDS)
      //case t if t =:= typeOf[Seq[Boolean]]  => underlying.getBooleanList(path).asScala.toSeq
      //case t if t =:= typeOf[Seq[Number]]   => underlying.getNumberList(path).asScala.toSeq
      //case t if t =:= typeOf[Seq[Int]]      => underlying.getIntList(path).asScala.toSeq
      //case t if t =:= typeOf[Seq[Long]]     => underlying.getLongList(path).asScala.toSeq
      //case t if t =:= typeOf[Seq[Double]]   => underlying.getDoubleList(path).asScala.toSeq
      //case t if t =:= typeOf[Seq[String]]   => underlying.getStringList(path).asScala.toSeq
      case t if t <:< typeOf[Seq[Boolean]]  => underlying.getBooleanList(path).asScala.toSeq
      case t if t <:< typeOf[Seq[Number]]   => underlying.getNumberList(path).asScala.toSeq
      case t if t <:< typeOf[Seq[Int]]      => underlying.getIntList(path).asScala.toSeq
      case t if t <:< typeOf[Seq[Long]]     => underlying.getLongList(path).asScala.toSeq
      case t if t <:< typeOf[Seq[Double]]   => underlying.getDoubleList(path).asScala.toSeq
      case t if t <:< typeOf[Seq[String]]   => underlying.getStringList(path).asScala.toSeq
      case t if t =:= typeOf[String]         => underlying.getString(path)
      case t if t =:= typeOf[Number]         => underlying.getNumber(path)
      case t if t =:= typeOf[Int]            => underlying.getInt(path)
      case t if t =:= typeOf[Long]           => underlying.getLong(path)
      case t if t =:= typeOf[Double]         => underlying.getDouble(path)
      case t if t =:= typeOf[Boolean]        => underlying.getBoolean(path)
      case t if t =:= typeOf[Config]         => underlying.getConfig(path)
      case t => 
        // Try to cast the string value from 
        throw new RuntimeException(s"""Type "${t}" is not supported to get.""")
    })
      .asInstanceOf[T]
  }

  def getOption[T: WeakTypeTag](path: String) = {
    if(underlying.hasPath(path)) Some(get[T](path))
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

  def getConfigOption(path: String): Option[Config] = 
    if(underlying.hasPath(path)) Some(underlying.getConfig(path))
    else None

  def getConfigOrEmpty(path: String): Config = 
    getConfigOption(path).getOrElse(ConfigFactory.empty())

  /**
   * Get value on path as Config OBJECT
   *
   * {{{
   *   val config = ConfigFactory.parseString("""hoge = hoge""")
   *
   *   config.getAsConfig("hoge", "some") // {some = hoge}
   * }}}
   */
  def getAsConfig(path: String, innerPath: String): Config = {
    val value = underlying.getValue(path)
    value.valueType match {
      case ConfigValueType.OBJECT => underlying.getConfig(path)
      case _ =>  value.atPath(innerPath)
    }
  }

  /**
   *
   */
  def toMap: Map[String, ConfigValue] = {
    underlying.root().entrySet.map(e => (e.getKey(), e.getValue())).toMap 
  }
}
