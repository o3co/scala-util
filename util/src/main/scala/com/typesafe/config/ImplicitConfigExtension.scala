package com.typesafe.config

/**
 * trait to provide implicit extensions of config
 */
trait ImplicitConfigExtensions {

  /**
   *
   */
  implicit class ScalaLikeConfig(val underlying: Config) extends ExtendedConfig 
}

/**
 * Object to provide implicits directly 
 */
object ImplicitConfigExtensions extends ImplicitConfigExtensions 
