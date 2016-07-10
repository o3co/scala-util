package jp.o3co.config

import akka.actor.ActorRefFactory
import akka.actor.ActorSelection
import com.typesafe.config.Config
import scala.language.reflectiveCalls
import scala.util.{Success, Failure}
import scala.concurrent.ExecutionContext
import com.typesafe.scalalogging.LazyLogging

trait BaseActorSelectionSettings extends Settings {
  /**
   * Get ActorSelection of the endpoint
   */
  def endpoint(implicit actorRefFactory: ActorRefFactory): ActorSelection
}

trait ActorSelectionSettings extends BaseActorSelectionSettings {
  /**
   *
   */
  def endpoint(implicit actorRefFactory: ActorRefFactory) = {
    actorRefFactory.actorSelection(config.getString("path"))
  }
}

/**
 * BootableActorSelectionSettings is a Settings specified on AdapterSettings usage. 
 * This settings provides the functionally to select or boot the child service actor for callee.
 */
trait BootableActorSelectionSettings extends BaseActorSelectionSettings {
  import akka.actor.ActorSelection
  import akka.actor.ActorRefFactory


  def propsFactory: BootableActorSelectionSettings.PropsFactory
 
  def actorName: Option[String]

  implicit def executionContext: ExecutionContext

  /**
   * Default endpoint
   */
  def endpoint(implicit actorRefFactory: ActorRefFactory) = selection("path", actorName, propsFactory) 

  /**
   * Causion:
   *   The actor may or may not booted when method return the actorSelection.
   */
  def selection(pathAt: String, actorName: Option[String], propsFactory: BootableActorSelectionSettings.PropsFactory)(implicit actorRefFactory: ActorRefFactory): ActorSelection = {
    if(config.hasPath(pathAt)) {
      actorRefFactory.actorSelection(config.getString(pathAt))
    } else if(actorName.nonEmpty) {
      // Create ActorSelection of the actorName
      val actor = actorRefFactory.actorSelection(actorName.get)
      actor.resolveOne(timeout).onComplete {
          case Success(ref) => //
          case Failure(e)   =>
            // Boot the actor with the name. So should boot when next actorSelection solve the path 
            actorRefFactory.actorOf(propsFactory.props(config), actorName.get)
      }
      // Return ActorSelection even this is not configured or not.
      actor
    } else {
      //actorRefFactory.actorOf(propsFactory.props(config))
      throw new Exception("""Either "path" on config or "actorName" on parameter need to be specified for selection.""") 
    }
  }
}

object BootableActorSelectionSettings {
  import akka.actor.Props
  /**
   * Type PropsFactory which contains props method
   */
  type PropsFactory = { def props(config: Config): Props }
}
