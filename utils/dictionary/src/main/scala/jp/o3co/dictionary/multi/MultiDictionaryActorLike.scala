package jp.o3co.dictionary
package multi

import akka.actor.Actor
import akka.pattern.pipe
import scala.concurrent.ExecutionContext

/**
 *
 */
trait MultiDictionaryActorLike[A, K, V] extends WithMultiDictionaryProtocol[A, K, V] {
  this: Actor with AsyncMultiDictionary[A, K, V] => 

  implicit def executionContext: ExecutionContext

  import protocol._

  def receiveDictionaryCommand: Receive = {
    case Contains(key, selector) => 
      containsAsync(key, selector)
        .map(exists => ContainsComplete(exists))
        .recover {
          case e: Throwable => ContainsFailure(e)
        }
        .pipeTo(sender)
    case GetKeysOf(term, selector) => 
      keysOfAsync(term, selector)
        .map(keys => GetKeysOfComplete(keys))
        .recover {
          case e: Throwable => GetKeysOfFailure(e)
        }
        .pipeTo(sender)
  }
}

