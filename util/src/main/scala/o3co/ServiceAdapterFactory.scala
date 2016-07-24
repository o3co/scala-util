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

trait ServiceAdapterFactory[T] extends actor.ActorAdapterFactory[T] {

  def apply(endpoint: ActorSelection)(implicit ec: ExecutionContext, to: Timeout): T 

  def apply(config: Config)(implicit arf: ActorRefFactory, ec: ExecutionContext): T = 
    adapter(config)
  
  def apply()(implicit arf: ActorRefFactory, ec: ExecutionContext): T = 
    adapter()

  def adapter(endpoint: ActorSelection)(implicit ec: ExecutionContext, to: Timeout): T = 
    apply(endpoint)
}
