package jp.o3co.tag
package dictionary
package store

import akka.actor.Actor
import akka.pattern.pipe
import com.typesafe.config.Config
import jp.o3co.config.jdbc._
import jp.o3co.datastore._
import scala.concurrent.Future

object TagDictionaryStoreActor {
  
  def props(config: Config) = {
    val settings = TagDictionaryStoreSettings(config)

    settings.dbType match {
      case DBTypes.MySQL  => MySQLSlickActor.props(settings)
      case _ => throw new Exception("Not supported")
    }
  }
}

trait TagDictionaryStoreActorLike extends KeyValueStoreActorLike[TermPK, TermEntity] {
  this: Actor with BaseTagDictionaryStore => 

  override val protocol: Protocol = Protocol

  import protocol._

  override def receiveStoreCommand = super.receiveStoreCommand orElse {
    case GetLabel(key, segment) => 
      getLabel(key, segment)
        .map(label => GetLabelComplete(label))
        .recover {
          case e: Throwable => GetLabelFailure(e)
        }
        .pipeTo(sender)
    case GetLabelKeys(label, segment) => 
      getLabelKeys(label, segment)
        .map(keys => GetLabelKeysComplete(keys))
        .recover {
          case e: Throwable => GetLabelKeysFailure(e)
        }
        .pipeTo(sender)
    case PutLabel(key, segment, label) => 
      putLabel(key, segment, label)
        .map(prev => PutLabelComplete(prev))
        .recover {
          case e: Throwable => PutLabelFailure(e)
        }
        .pipeTo(sender)
    case DeleteLabel(key, segment) => 
      deleteLabel(key, segment)
        .map(deleted => DeleteLabelComplete(deleted))
        .recover {
          case e: Throwable => DeleteLabelFailure(e)
        }
        .pipeTo(sender)
  }
}

