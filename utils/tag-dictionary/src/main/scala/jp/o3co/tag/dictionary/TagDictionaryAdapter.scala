package jp.o3co.tag
package dictionary

import akka.actor.ActorRefFactory
import akka.actor.ActorSelection
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.Config
import jp.o3co.config.Settings
import jp.o3co.dictionary.multi.MultiDictionaryAdapterLike
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.util.{Success, Failure}

import Protocol._


case class TagDictionaryAdapterSettings(config: Config) extends Settings {

  def isRemote: Boolean = config.hasPath("path")

  def path: String = config.get[String]("path")

  def initialTimeout = config.get[FiniteDuration]("initial-timeout")

  def name: String = config.getOrElse[String]("name", "TagDictionary")
}

/**
 * TagDictionaryAdapter  
 */
case class TagDictionaryAdapter(config: Config)(implicit actorRefFactory: ActorRefFactory) extends TagDictionaryAdapterLike {

  override val protocol = Protocol

  val settings = TagDictionaryAdapterSettings(config)

  implicit val executionContext: ExecutionContext = actorRefFactory.dispatcher

  /** 
   * create child actor to communicate with the actual tag dictionary 
   */
  val endpoint: ActorSelection = 
    if(settings.isRemote) {
      actorRefFactory.actorSelection(settings.path)
    } else {
      // Try to lookup child actor
      val ref = actorRefFactory.actorSelection("./" + settings.name)

      // Try resolve first, and if not exists, create on child
      ref
        .resolveOne(settings.initialTimeout)
        .onComplete {
          case Success(ref) => // Do nothing
          case Failure(e)   => 
            actorRefFactory.actorOf(LocalTagDictionaryActor.props(config), settings.name)
        }
      ref
    }

  implicit val timeout: Timeout = settings.timeout 
}

trait TagDictionaryAdapterLike extends MultiDictionaryAdapterLike[TagSegment, TagName, TagLabel] {

  def getNamesFor(labels: Traversable[TagLabel], segment: Option[TagSegment] = None) = {
    (endpoint ? GetNamesFor(labels, segment))
      .map {
        case GetNamesForComplete(names)  => names 
        case GetNamesForFailure(cause)   => throw cause
      }
  }

  def getLabelsFor(names: Traversable[TagName], segment: Option[TagSegment] = None) = {
    (endpoint ? GetLabelsFor(names, segment))
      .map {
        case GetLabelsForComplete(labels) => labels
        case GetLabelsForFailure(cause)   => throw cause
      }
  }
}
