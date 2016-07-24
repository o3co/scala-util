package o3co.actor

import akka.actor.ActorRef
import akka.actor.ActorRefFactory
import akka.actor.ActorSelection
import akka.util.Timeout
import com.typesafe.config.Config
import com.typesafe.config.ImplicitConfigExtensions._
import o3co.config.GlobalSettings
import o3co.config.ServiceAdapterSettings
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.ExecutionContext

/**
 *
 */
trait ActorAdapterFactory[T] {
  
  /**
   * Override this to support `adapter()`
   */
  def defaultConfigPath: String = null

  /**
   * Create adapter from ActorSelection
   */
  def adapter(endpoint: ActorSelection)(implicit ec: ExecutionContext, timeout: Timeout): T

  /**
   * Create adapter from ActorRef
   */
  def adapter(endpoint: ActorRef)(implicit arf: ActorRefFactory, ec: ExecutionContext, timeout: Timeout): T = {
    adapter(arf.actorSelection(endpoint.path))
  }

  /**
   * Create adapter from configuration 
   */
  def adapter(config: Config)(implicit arf: ActorRefFactory, ec: ExecutionContext): T = {
    val settings = ServiceAdapterSettings(config) 
    adapter(settings.endpoint)(ec, settings.timeout)
  }

  /**
   * Create adapter from default configuration 
   */
  def adapter()(implicit arf: ActorRefFactory, ec: ExecutionContext): T = 
    Option(defaultConfigPath) match {
      case Some(c) => adapter(GlobalSettings.config.getAsConfig(c, "path"))
      case None    => throw new Exception("Creating adapter without default configuration path is not supported.")
    }
} 
