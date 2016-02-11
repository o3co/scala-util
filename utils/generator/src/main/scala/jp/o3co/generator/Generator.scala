package jp.o3co.generator

import scala.util.Try

/**
 * Base trait of Generator(SyncGenerator)
 */
trait Generator[T] {

  /**
   * 
   */
  def generate(): T

  /**
   * Create ValidateGenerator with this generator 
   */
  def withValidation(f: T => Boolean) = ValidateGenerator(this, f)
}

