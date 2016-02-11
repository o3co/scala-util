package jp.o3co.tag
package dictionary
package key

import akka.actor.ActorSelection
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.Config
import jp.o3co.config.Settings
import jp.o3co.generator._
import jp.o3co.generator.impl.RandomStringGenerator
import scala.concurrent.ExecutionContext
import scala.concurrent.Await

import Protocol._

case class TagNameGeneratorSettings(config: Config) extends Settings {
  def length: Int  = config.getOrElse[Int]("length", 16)

  def retries: Int = config.getOrElse[Int]("retries", 3)

  val validationTimeout = config.getFiniteDuration("validation-timeout")
}

/**
 *
 */
class TagNameGenerator(config: Config, store: TagDictionaryStore)(implicit val executionContext: ExecutionContext) extends ValidateGenerator[TagName] {

  val settings = TagNameGeneratorSettings(config)

  implicit val validationTimeout = settings.validationTimeout

  override val defaultValidator: Validator[TagName] = { generated => 
    
    try {
      Await.result(store.containsLabelKey(generated, None), validationTimeout)
    } catch {
      case e: Throwable => false
    }
  }

  override val numOfRetries = settings.retries

  /**
   * underlying name geneartor
   */
  val nameGenerator = RandomStringGenerator(settings.length, RandomStringGenerator.ValueTypes.AlphaNumeric)
  
  protected def doGenerate: TagName = TagName(nameGenerator.generate)
}

object TagNameGenerator {
  /**
   *
   */
  def apply(config: Config, store: TagDictionaryStore)(implicit ec: ExecutionContext) = new TagNameGenerator(config, store)
}
