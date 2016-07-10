package jp.o3co.id.provider
package impl

import akka.actor.ActorRefFactory
import java.util.UUID
import jp.o3co.config.Settings
import jp.o3co.generator.impl.RandomUUIDGenerator
import jp.o3co.generator.ValidateGenerator
import jp.o3co.id.store.RedisIDStore
import scala.concurrent.Await
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.ExecutionContext

/**
 * Sample RedisUUIDProviderImpl
 */
trait RedisUUIDProviderImpl extends StoredIDProviderImpl[UUID] {

  def settings: Settings

  implicit class RedisUUIDProviderImplSettings(val settings: Settings) {
    def validationTimeout: FiniteDuration = settings.getOrElse("validation-timeout", settings.timeout)
  }

  def validationTimeout: FiniteDuration

  implicit def actorRefFactory: ActorRefFactory 

  implicit def executionContext: ExecutionContext 

  /**
   * Base UUID Generator
   */
  val baseGenerator = RandomUUIDGenerator()

  /**
   * ValidateGenerator to validate generated UUID is not exists yet
   */
  val generator = ValidateGenerator[UUID](baseGenerator, { id => 
    !Await.result(idStore.existsAsync(id), validationTimeout)
  })

  /**
   *
   */
  val idStore = RedisIDStore[UUID](settings.getConfig("redis"))
}
