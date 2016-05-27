package com.typesafe.config

import scala.concurrent.duration._
import scala.reflect.runtime.universe._


/**
 * Add functionality to get Predef types
 */
trait ConfigExtensionMethods {
  /**
   * Base configuration
   */
  def config: Config

  /**
   * Get FiniteDuration
   */
  def getFiniteDuration(path: String): FiniteDuration = 
    FiniteDuration(config.getDuration(path, MILLISECONDS), MILLISECONDS)

}


/**
 * Bind all Extensions together
 */
trait ConfigExtensions extends ScalaLikeOperations with ConfigExtensionMethods {
  /**
   *
   */
  override def get[T: WeakTypeTag](path: String): T = {
    (weakTypeOf[T] match {
      case t if t =:= typeOf[FiniteDuration] => getFiniteDuration(path)
      case t => super.get[T](path)
    }).asInstanceOf[T]
  }
}
