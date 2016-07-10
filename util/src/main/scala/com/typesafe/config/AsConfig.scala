package com.typesafe.config

/**
 *
 */
trait AsConfig {
  
  /**
   * Underlying configuration
   */
  def underlying: Config 
}
