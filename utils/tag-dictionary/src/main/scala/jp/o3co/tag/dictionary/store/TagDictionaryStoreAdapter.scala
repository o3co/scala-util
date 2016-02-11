package jp.o3co.tag
package dictionary
package store

import akka.actor.ActorRefFactory
import akka.actor.ActorSelection
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.Config
import scala.concurrent.ExecutionContext
import jp.o3co.datastore.KeyValueStoreAdapterLike
import jp.o3co.config.Settings
import scala.util.{Success, Failure}
import scala.concurrent.duration.FiniteDuration

case class TagDictionaryStoreAdapterSettings(config: Config) extends Settings {
  val initialTimeout: FiniteDuration = config.getOrElse("initial-timeout", timeout)

  lazy val name: String = config.getOrElse("name", "store")
}

case class TagDictionaryStoreAdapter(config: Config)(implicit val actorRefFactory: ActorRefFactory) extends TagDictionaryStoreAdapterLike {
  
  override val protocol: Protocol = Protocol

  val settings = TagDictionaryStoreAdapterSettings(config)

  val endpoint: ActorSelection = actorRefFactory.actorSelection("./" + settings.name)

  implicit val executionContext = actorRefFactory.dispatcher

  implicit val timeout: Timeout = settings.timeout

  // Try resolve actorSelection and if cannot resolve, then boot the actor 
  endpoint.resolveOne(settings.initialTimeout).onComplete {
    case Success(ref) => //
    case Failure(e)   => 
      actorRefFactory.actorOf(TagDictionaryStoreActor.props(config))
  }
}

trait TagDictionaryStoreAdapterLike extends BaseTagDictionaryStore with KeyValueStoreAdapterLike[TermPK, TermEntity] {
  
  val protocol: Protocol 

  import protocol._

  def endpoint: ActorSelection

  def containsLabelKey(key: TagName, segment: Option[TagSegment]) = {
    (endpoint ? ContainsLabelKey(key, segment))
      .map {
        case ContainsLabelKeyComplete(exists) => exists 
        case ContainsLabelKeyFailure(cause)  => throw cause
      }
  }

  def getLabel(key: TagName, segment: TagSegment) = {
    (endpoint ? GetLabel(key, segment))
      .map {
        case GetLabelComplete(label) => label
        case GetLabelFailure(cause)  => throw cause
      }
  }

  def getLabelKeys(label: TagLabel, segment: TagSegment) = {
    (endpoint ? GetLabelKeys(label, segment))
      .map {
        case GetLabelKeysComplete(keys) => keys 
        case GetLabelKeysFailure(cause)  => throw cause
      }
  }

  def putLabel(key: TagName, segment: TagSegment, label: TagLabel) = {
    (endpoint ? PutLabel(key, segment, label))
      .map {
        case PutLabelComplete(labels) => labels
        case PutLabelFailure(cause)  => throw cause
      }
  }

  def deleteLabel(key: TagName, segment: TagSegment) = {
    (endpoint ? DeleteLabel(key, segment))
      .map {
        case DeleteLabelComplete(labels) => labels
        case DeleteLabelFailure(cause)  => throw cause
      }
  }
}
