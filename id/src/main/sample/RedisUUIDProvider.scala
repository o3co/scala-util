package sample

import akka.actor.ActorRefFactory
import java.util.UUID
import o3co.config.Settings
import o3co.generate.impl.RandomUUIDGenerator
import o3co.generate.ValidateGenerator
import o3co.id.store.RedisIDStore
import scala.concurrent.Await
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.ExecutionContext

/**
 * Sample RedisUUIdProviderImpl
 */
trait RedisUUIdProviderImpl extends StoredIdProviderImpl[UUID] {

  def settings: Settings

  implicit class RedisUUIdProviderImplSettings(val settings: Settings) {
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

