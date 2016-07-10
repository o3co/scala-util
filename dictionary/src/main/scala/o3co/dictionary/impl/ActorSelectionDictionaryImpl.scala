package o3co.dictionary
package impl

import akka.actor.ActorSelection
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.ExecutionContext

trait ActorSelectionDictionaryImpl[K, V] extends AsyncDictionary[K, V] {

  val protocol: DictionaryProtocol[K, V]
  import protocol._

  def endpoint: ActorSelection

  implicit def executionContext: ExecutionContext

  implicit def timeout: Timeout 

  def findKeysAsync(value: V) = {
    (endpoint ? FindKeys(value))
      .map {
        case FindKeysSuccess(keys)  => keys
        case FindKeysFailure(cause)  => throw cause
      }
  }

  /**
   */
  def getAsync(key: K) = {
    (endpoint ? Get(key))
      .map {
        case GetSuccess(v) => v 
        case GetFailure(cause)  => throw cause
      }
  }

  /**
   *
   */
  def putAsync(key: K, value: V) = {
    (endpoint ? Put(key, value))
      .map {
        case PutSuccess()  => // 
        case PutFailure(cause)  => throw cause
      }
  }

  /**
   *
   */
  def removeAsync(key: K) = {
    (endpoint ? Remove(key))
      .map {
        case RemoveSuccess() => // Unit 
        case RemoveFailure(cause)  => throw cause
      }
  }

  def keysAsync = {
    (endpoint ? Keys())
      .map {
        case KeysSuccess(keys) => keys
        case KeysFailure(cause) => throw cause 
      }
  }

  def valuesAsync = {
    (endpoint ? Values())
      .map {
        case ValuesSuccess(keys) => keys
        case ValuesFailure(cause) => throw cause 
      }
  }
}
