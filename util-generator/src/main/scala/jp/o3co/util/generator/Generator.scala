package jp.o3co.util.generator

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

  /**
   * Method to generate single value 
   */
  protected def doGenerate: T

  /**
   *
   * @return T Generated value
   * @throws GenerateException thrown if Failed to generate valid value in tries
   */
  def generate: T = tryGenerate.getOrElse(throw new GenerateException("Failed to generate value."))

  /**
   * Try generate
   *
   * @return Option[T] Some if successfully generated, None otherwise
   */
  def tryGenerate: Option[T] = {
    Iterator.continually(Try(doGenerate))
      .take(numOfTries)
      .find(t => t.isSuccess && validation(t.get))
      .flatMap(_.toOption)
  }
}
