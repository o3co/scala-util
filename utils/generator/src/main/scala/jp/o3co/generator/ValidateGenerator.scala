package jp.o3co.generator

import scala.util.Try

/**
 * Generate value with Validation
 */
trait ValidateGenerator[T] extends Generator[T] {

  type Validation = (T) => Boolean

  /**
   *
   */
  val numOfRetries: Int   = 0

  /**
   *
   */
  val defaultValidator: Validation = Validator.noValidation[T]

  /**
   * {@inheritDoc}
   */
  def generate() = generate(defaultValidator)

  /**
   * Generate value with Validation
   *
   * @return T Generated value
   * @throws GeneratorException thrown if Failed to generate valid value in tries
   */
  def generate(validator: Validation) = tryGenerate(validator).getOrElse(throw new GeneratorException("Failed to generate valid value."))

  /**
   * Try generate
   *
   * @return Option[T] Some if successfully generated, None otherwise
   */
  def tryGenerate(implicit validator: Validation): Option[T] = {
    Iterator.continually(Try(doGenerate))
      .take(numOfRetries + 1)
      .find(t => t.isSuccess && validator(t.get))
      .flatMap(_.toOption)
  }

  protected def doGenerate: T
}

object ValidateGenerator {
  /**
   *
   */
  def apply[T](generator: Generator[T], validation: T => Boolean) = new impl.GeneratorProxy[T] with ValidateGenerator[T] {
    override val defaultValidator = validation
    override val underlying       = generator

    override def generate() = generate(defaultValidator)

    override protected def doGenerate = underlying.generate()
  }
}
