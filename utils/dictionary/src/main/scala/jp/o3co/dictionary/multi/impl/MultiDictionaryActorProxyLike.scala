package jp.o3co.dictionary
package multi
package impl 

import akka.actor.ActorSelection
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.ExecutionContext

/**
 *
 */
trait MultiDictionaryActorProxyLike[A, K, V] extends AsyncMultiDictionary[A, K, V] {

  val protocol: MultiDictionaryProtocol[A, K, V]

  import protocol._

  /**
   *
   */
  def endpoint: ActorSelection

  implicit def timeout: Timeout

  implicit def executionContext: ExecutionContext

  def getDictionary(selector: A) = Option(AsyncSelectedDictionary(this, selector))

  def containsAsync(key: K, selector: Option[A]) = 
    (endpoint ? Contains(key, selector))
        .map {
          case ContainsComplete(exists) => exists
          case ContainsFailure(e) => throw e
        }

  /**
   *
   */
  def keysOfAsync(term: V, selector: A) = 
    (endpoint ? GetKeysOf(term, selector))
      .map {
        case GetKeysOfComplete(keys) => keys 
        case GetKeysOfFailure(cause) => throw cause
      }

  /**
   *
   */
  def getTermAsync(key: K, selector: A) = 
    (endpoint ? GetTerm(key, selector))
      .map {
        case GetTermComplete(term)  => term 
        case GetTermFailure(cause)  => throw cause
      }

  /**
   *
   */
  def putTermAsync(key: K, term: V, selector: A) = 
    (endpoint ? PutTerm(key, term, selector))
      .map {
        case PutTermComplete(prev)   => prev 
        case PutTermFailure(cause)   => throw cause
      }

  def deleteTermAsync(key: K, selector: A) = 
    (endpoint ? DeleteTerm(key, selector))
      .map {
        case DeleteTermComplete(deleted) => deleted 
        case DeleteTermFailure(cause)    => throw cause
      }
}
