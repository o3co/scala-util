package jp.o3co.dictionary

import akka.actor.Actor
import akka.actor.ActorRefFactory
import akka.actor.ActorSelection
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.Config
import jp.o3co.config.Settings
import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.reflect.runtime.universe._

trait DictionaryAdapterSettingsLike extends Settings {
  
  lazy val endpoint: Config = config.getAsConfig("endpoint", "path")
}

trait DictionaryAdapterLike[K, V] extends Dictionary[K, V] with AsyncDictionary[K, V] {

  def settings: DictionaryAdapterSettingsLike

  val protocol: DictionaryProtocol[K, V]

  import protocol._

  def actorRefFactory: ActorRefFactory

  implicit def executionContext: ExecutionContext

  implicit def timeout: Timeout

  lazy val endpoint = actorRefFactory.actorSelection(settings.endpoint.getString("path"))

  def keysOf(term: Term) = Await.result(keysOfAsync(term), settings.timeout)

  def keysOfAsync(term: Term): Future[Iterable[TermKey]] = {
    (endpoint ? GetKeysOf(term))
      .map {
        case GetKeysOfComplete(keys)  => keys
        case GetKeysOfFailure(cause)  => throw cause
      }
  }

  def getTerm(key: TermKey) = Await.result(getTermAsync(key), settings.timeout)

  /**
   * Get Term if exists
   */
  def getTermAsync(key: TermKey) = {
    (endpoint ? GetTerm(key))
      .map {
        case GetTermComplete(terms) => terms
        case GetTermFailure(cause)  => throw cause
      }
  }

  def putTerm(key: TermKey, term: Term) = Await.result(putTermAsync(key, term), settings.timeout)

  /**
   *
   */
  def putTermAsync(key: TermKey, term: Term) = {
    (endpoint ? PutTerm(key, term))
      .map {
        case PutTermComplete(prev)  => prev 
        case PutTermFailure(cause)  => throw cause
      }
  }

  def deleteTerm(key: TermKey) = Await.result(deleteTermAsync(key), settings.timeout)

  /**
   *
   */
  def deleteTermAsync(key: TermKey) = {
    (endpoint ? DeleteTerm(key))
      .map {
        case DeleteTermComplete(deleted) => deleted
        case DeleteTermFailure(cause)  => throw cause
      }
  }
}

