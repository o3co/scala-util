package jp.o3co.id.provider

import akka.actor.Actor
import akka.actor.ActorRefFactory
import com.typesafe.config.Config
import java.util.UUID
import jp.o3co.config.Settings

/**
 *
 */
class RedisUUIDProviderActor(config: Config) extends IDProviderActor[UUID] with impl.RedisUUIDProviderImpl {

  val protocol = RedisUUIDProviderActor.Protocol

  val settings = Settings(config)

  val validationTimeout = settings.validationTimeout

  val actorRefFactory: ActorRefFactory = context

  val executionContext = context.dispatcher
}

/**
 *
 */
object RedisUUIDProviderActor {
  /**
   * Default protocol of RedisUUIDProviderActor
   */
  object Protocol extends IDProviderProtocol[UUID] 
}
