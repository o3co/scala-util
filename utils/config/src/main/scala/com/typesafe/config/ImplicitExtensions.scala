package com.typesafe.config

/**
 *
 */
trait ImplicitExtensions {

  /**
   *
   */
  implicit class ScalaLikeConfig(override val config: Config) extends ConfigExtensions 
}

object ImplicitExtensions extends ImplicitExtensions

