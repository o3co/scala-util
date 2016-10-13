package com.typesafe.config

/**
 * package com.typesafe.config.global
 */
package object global {

  // Application config
  lazy val ApplicationConfig: Config = 
    ConfigFactory
      .load()
      .withFallback(ConfigFactory.defaultReference())

  /**
   * Default configuration
   */
  lazy val Defaults: Config = {
    if(ApplicationConfig.hasPath("defaults")) ApplicationConfig.getConfig("defaults")
    else ConfigFactory.empty()
  }
}
