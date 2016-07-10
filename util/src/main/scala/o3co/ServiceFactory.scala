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
trait ServiceBooter {

  /**
   *
   */
  def defaultServiceClassName : String = ""

  def defaultServiceClass = Class.forName(defaultServiceClassName)

  def initialTimeout: FiniteDuration = 3.seconds 

  def props(config: Config): Props = {
    val serviceClass = if(config.hasPath("service-class")) {
      Class.forName(config.getString("service-class"))
    } else {
      defaultServiceClass
    }

    Props(serviceClass, config)
  }
    
  /**
   * Boot servie actor
   */
  def boot(config: Config, name: String = null)(implicit actorRefFactory: ActorRefFactory): ActorRef = 
    Option(name) match {
      case Some(n) => actorRefFactory.actorOf(props(config), n)
      case None    => actorRefFactory.actorOf(props(config))
    }

  def bootOnce(config: Config, actorName: String)(implicit actorRefFactory: ActorRefFactory, executionContext: ExecutionContext): Future[ActorRef] = {
    // Try select for the actorName first
    actorRefFactory.actorSelection(actorName)
      .resolveOne(initialTimeout)
      .recover {
        case e: Throwable => 
          // If not resolvable, then boot new actor
          boot(config, actorName)
      }
  }
}

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
trait ServiceFactory[T] extends ServiceBooter {
  import scala.language.reflectiveCalls

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
