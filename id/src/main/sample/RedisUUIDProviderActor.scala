package sample

import akka.actor.Actor
import akka.actor.ActorRefFactory
import com.typesafe.config.Config
import java.util.UUID
import o3co.config.Settings

/**
 *
 */
class RedisUUIdProviderActor(config: Config) extends IdProviderActor[UUID] with impl.RedisUUIdProviderImpl {

  val protocol = RedisUUIdProviderActor.Protocol

  val settings = Settings(config)

  val validationTimeout = settings.validationTimeout

  val actorRefFactory: ActorRefFactory = context

  val executionContext = context.dispatcher
}

/**
 *
 */
object RedisUUIdProviderActor {
  /**
   * Default protocol of RedisUUIdProviderActor
   */
  object Protocol extends IdProviderProtocol[UUID] 
}

