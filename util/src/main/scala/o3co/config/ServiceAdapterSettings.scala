package o3co.config

import akka.actor.ActorRefFactory
import com.typesafe.config.Config

trait ServiceAdapterSettingsLike extends BaseServiceSettingsLike {
  this: Settings => 

  def path = config.get[String]("path")

  def endpoint(implicit actorRefFactory: ActorRefFactory) = actorRefFactory.actorSelection(path)

  def adapterClass: Class[_]= Class.forName(config.get[String]("adapter-class"))
}

object ServiceAdapterSettings {
  def apply(c: Config) = new Settings with ServiceAdapterSettingsLike {
    val config = c
  }
}
