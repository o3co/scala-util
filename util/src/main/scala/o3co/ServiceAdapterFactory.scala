package o3co

import akka.actor.ActorRefFactory
import akka.actor.ActorSelection
import akka.util.Timeout
import com.typesafe.config.Config
import com.typesafe.config.ImplicitConfigExtensions._
import scala.concurrent.ExecutionContext
import o3co.config.ServiceAdapterSettings
import o3co.config.GlobalSettings
import scala.concurrent.duration.FiniteDuration

trait ServiceAdapterFactoryLike[T] {

  def defaultConfigPath: String 

  def adapter(endpoint: ActorSelection)(implicit ec: ExecutionContext, timeout: Timeout): T

  def adapter(config: Config)(implicit arf: ActorRefFactory, ec: ExecutionContext): T = {
    val settings = ServiceAdapterSettings(config) 
    //settings.adapterClass
    //  .getDeclaredConstructor(ActorSelection.getClass, Timeout.getClass, ExecutionContext.getClass)
    //  .newInstance(settings.endpoint, settings.timeout, ec)
    //  .asInstanceOf[T]
    adapter(settings.endpoint)(ec, settings.timeout)
  }

  def adapter()(implicit arf: ActorRefFactory, ec: ExecutionContext): T = 
      adapter(GlobalSettings.config.getAsConfig(defaultConfigPath, "path"))
}

trait ServiceAdapterFactory[T] extends ServiceAdapterFactoryLike[T] {

  def apply(endpoint: ActorSelection)(implicit ec: ExecutionContext, to: Timeout): T 

  def apply(config: Config)(implicit arf: ActorRefFactory, ec: ExecutionContext): T = 
    adapter(config)
  
  def apply()(implicit arf: ActorRefFactory, ec: ExecutionContext): T = 
    adapter()

  def adapter(endpoint: ActorSelection)(implicit ec: ExecutionContext, to: Timeout): T = 
    apply(endpoint)
}
