package jp.o3co.tag.dictionary 

import akka.actor.Actor
import akka.pattern.pipe
import jp.o3co.dictionary.multi.MultiDictionaryActorLike

/**
 *
 */
trait TagDictionaryActorLike extends MultiDictionaryActorLike[TagSegment, TagName, TagLabel] {
  this: Actor with TagDictionary => 

  val protocol: Protocol = Protocol

  import protocol._

  def receiveTagDictionaryCommand: Receive = {
    case GetNamesFor(labels, segment) => 
      getNamesFor(labels, segment)
        .map(names => GetNamesForComplete(names)) 
        .recover {
          case e: Throwable => GetNamesForFailure(e)
        }
        .pipeTo(sender)
    case GetLabelsFor(names, segment) => 
      getLabelsFor(names, segment)
        .map(labels => GetLabelsForComplete(labels))
        .recover {
          case e: Throwable => GetLabelsForFailure(e)
        }
        .pipeTo(sender)
  }
}

