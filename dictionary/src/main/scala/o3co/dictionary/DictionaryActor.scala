package o3co.dictionary

import akka.actor.Actor
import akka.pattern.pipe
import scala.concurrent.ExecutionContext
import scala.util.Try

/**
 *
 */
trait BaseDictionaryActorLike[K, V] {
  this: Actor with BaseDictionary[K, V] => 

  implicit def executionContext: ExecutionContext

  val protocol: BaseDictionaryProtocolLike[K, V]
  import protocol._

  def receiveBaseDictionaryCommands: Receive = {
    case ContainsKey(key) => 
      sender() ! Try(containsKey(key))
        .map(r => ContainsKeySuccess(r))
        .recover {
          case e: Throwable => ContainsKeyFailure(e)
        }
    case ContainsValue(value) => 
      sender() ! Try(containsValue(value))
        .map(r => ContainsValueSuccess(r))
        .recover {
          case e: Throwable => ContainsValueFailure(e)
        }
    case Get(key) =>
      sender() ! Try(get(key))
        .map(r => GetSuccess(r))
        .recover {
          case e: Throwable => GetFailure(e)
        }
    case Put(key, value) =>
      sender() ! Try(put(key, value))
        .map(_ => PutSuccess())
        .recover {
          case e: Throwable => PutFailure(e)
        }
    case Remove(key) =>
      sender() ! Try(remove(key))
        .map(_ => RemoveSuccess())
        .recover {
          case e: Throwable => RemoveFailure(e)
        }
    case Keys() =>
    case Values() =>
  }
}

