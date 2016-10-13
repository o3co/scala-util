package com.typesafe.config

import scala.reflect.runtime.universe._
import scala.concurrent.duration.FiniteDuration

/**
 * Extended configuration 
 */
trait ExtendedConfig extends AsConfig 
  with ScalaLikeOperations 
  with ConfigExtensionMethods
{
  
  /**
   *
   */
  override def get[T: WeakTypeTag](path: String): T = {
    (weakTypeOf[T] match {
      case t if t =:= typeOf[FiniteDuration] => getFiniteDuration(path)
      case t => super.get[T](path)
    }).asInstanceOf[T]
  }

  def withDefaults: Config = {
    underlying.withFallback(global.Defaults)
  }
}
