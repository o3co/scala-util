package o3co.util.generator

import scala.util.Try

/**
 *
 */
trait Generator[T] {

  type Validation = T => Boolean

  def numOfTries: Int = 1

  /**
   * validation 
   * By default, always return true
   */
  def validation: Validation = { value => 
    true
  }

  def validate(value: T): Boolean = {
    validation(value)
  }

  protected def doGenerate: T

  def generate: T = tryGenerate.getOrElse(throw new GenerateException("Failed to generate value."))
  
  def tryGenerate: Option[T] = {
    Iterator.continually(Try(doGenerate))
      .take(numOfTries)
      .find(t => t.isSuccess && validate(t.get))
      .flatMap(_.toOption)
  }
}
