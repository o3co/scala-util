package com.typesafe.config

import scala.concurrent.duration._
import scala.reflect.runtime.universe._


/**
 * Add functionality to get Predef types
 */
trait ConfigExtensionMethods extends AsConfig {

  /**
   * Get FiniteDuration
   */
  def getFiniteDuration(path: String): FiniteDuration = 
    FiniteDuration(underlying.getDuration(path, MILLISECONDS), MILLISECONDS)

}

