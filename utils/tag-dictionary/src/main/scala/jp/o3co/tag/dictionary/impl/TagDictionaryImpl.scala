package jp.o3co.tag
package dictionary
package impl

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import jp.o3co.generator.Generator

trait TagDictionaryImpl extends TagDictionary {

  implicit def executionContext: ExecutionContext

  def generator: Generator[TagName]

  def generateKey = generator.generate

  def defaultSegment: TagSegment

  /**
   *
   */
  def getNamesFor(labels: Traversable[TagLabel], segment: Option[TagSegment] = None) = 
    Future.sequence(labels
      .map { label => 
        keysOfAsync(label, segment.getOrElse(defaultSegment))
          .flatMap {
            case Seq(a, _*) => Future((label, a))
            case _ => 
              val newKey = generateKey
              putTermAsync(newKey, label, segment.getOrElse(defaultSegment))
                .map(_ => (label, newKey))
          }
      })
      .map { names => names.toMap }

  /*
   *
   */
  def getLabelsFor(names: Traversable[TagName], segment: Option[TagSegment] = None) =
    Future.sequence(names
      .map { name => 
        getTermAsync(name, segment.getOrElse(defaultSegment))
          .map(label => (name, label))
      })
      .map { labels => 
        labels
          .collect {
            case (name, Some(label)) => (name, label)
          }
          .toMap
      }
}
