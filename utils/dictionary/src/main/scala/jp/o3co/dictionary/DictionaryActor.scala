package jp.o3co.dictionary

import akka.actor.Actor
import akka.pattern.pipe
import scala.concurrent.ExecutionContext

/**
 *
 */
trait DictionaryActorLike[K, V] extends WithDictionaryProtocol[K, V] {
  this: Actor with AsyncDictionary[K, V] => 

  implicit def executionContext: ExecutionContext

  import protocol._

  def receiveDictionaryCommand: Receive = {
    case GetKeysOf(term) => 
      keysOfAsync(term)
        .map(keys => GetKeysOfComplete(keys))
        .recover {
          case e: Throwable => GetKeysOfFailure(e)
        }
        .pipeTo(sender)
  }
}

