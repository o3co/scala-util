package jp.o3co.id.store

import com.typesafe.config.Config
import akka.actor.ActorRefFactory
import scala.concurrent.ExecutionContext

/**
 *
 */
trait RedisIDStore[ID] extends IDStore[ID] with impl.RedisIDStoreImpl[ID]

/**
 *
 */
object RedisIDStore {
  
  def apply[ID](config: Config)(implicit ec: ExecutionContext, arf: ActorRefFactory) = new RedisIDStore[ID] {
    val actorRefFactory = arf
    val executionContext = ec

    val key = config.getString("key")
  }
}
