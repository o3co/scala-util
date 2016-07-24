package o3co

import akka.actor.ActorRef
import akka.actor.ActorRefFactory
import akka.actor.ActorSelection
import akka.actor.Props
import akka.util.Timeout
import com.typesafe.config.Config
import com.typesafe.config.ImplicitConfigExtensions._
import o3co.config.ServiceSettings
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.util.{Success, Failure}

/**
 * {{{
 *   object Service extends ServiceBooter {
 *      val DefaultServiceClass = "com.sample.ServiceActor"
 *   }
 *
 *   Service.boot(config, "service")  // will boot new actor "/user/service"
 * }}}
 */
/**
 * ServiceFactory is a factory to create an adapter which facade of newly created or existed actor.
 * This means that if the caller can be a parent of this target service, then use this factory.
 * Otherwise, use ServiceAdapterFactory instead.
 * ServiceAdapterFactory does not have functionality to boot the new actor.
 *
 * ServiceFactory strategy is 
 *   1 If "path" is defined on the config, then use path to select actor
 *   2 If "path" is not defined,
 *     a and if actor name is specfied, then boot once
 *     b or if actor name is not specified, then boot always.
 *
 * {{{
 *   object Service extends ServiceFactory[Service] {
 *     val defaultServiceClass = "com.sample.ServiceActor"
 *     val adapterFactory = com.sample.ServiceAdapter
 *   }
 *
 * }}}
 */
trait ServiceFactory[T] extends actor.ActorBoot {
  import scala.language.reflectiveCalls

  override val ClassPath = "service-class"

  def adapterFactory: ServiceAdapterFactory[T] 

  def apply(config: Config, actorName: String = null)(implicit actorRefFactory: ActorRefFactory, ec: ExecutionContext) = {
    val settings = ServiceSettings(config) 
    adapterFactory(selectOrBoot(config, actorName))(ec, settings.timeout)
  }

  /**
   *
   */
  def selectOrBoot(config: Config, name: String = null)(implicit actorRefFactory: ActorRefFactory, ec: ExecutionContext): ActorSelection = {
    if(config.hasPath("path")) {
      actorRefFactory.actorSelection(config.getString("path"))
    } else {
      Option(name) match {
        case None =>
          // Boot always
          actorRefFactory.actorSelection(boot(config).path)
        case Some(actorName) => 
          Await.result(bootOnce(config, actorName).map { ref => 
            actorRefFactory.actorSelection(ref.path)
          }, initialTimeout)
      }
    }
  }
}
