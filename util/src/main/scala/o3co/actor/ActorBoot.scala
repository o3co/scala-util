package o3co.actor

import akka.actor.ActorRef
import akka.actor.ActorRefFactory
import akka.actor.ActorSelection
import akka.actor.Props
import akka.util.Timeout
import com.typesafe.config.Config
import scala.concurrent.duration._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext

/**
 * Trait to implement boot strategy of Actor 
 *
 * {{{
 * object Service extends ServiceBoot {
 *   def DefaultClassName = "com.sample.ServiceActor"
 * }
 * // Boot service actor
 * val actorRef = Service.boot(config, "service")
 * }}}
 */
trait ActorBoot {

  def DefaultClassName: String = null

  def defaultClass = Option(DefaultClassName) match {
    case Some(c) => Class.forName(c)
    case None    => throw new Exception("Config 'class' is not specified to boot actor.")
  }

  def initialTimeout: FiniteDuration = 3.seconds 

  val ClassPath = "class"

  def props(config: Config): Props = {
    val clazz = if(config.hasPath(ClassPath)) {
      Class.forName(config.getString(ClassPath))
    } else {
      defaultClass
    }

    Props(clazz, config)
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
