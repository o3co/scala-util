package jp.o3co.tag.dictionary

import akka.actor.Actor
import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.Config
import scala.concurrent.Future
import jp.o3co.dictionary.multi.AsyncMultiDictionary
import jp.o3co.dictionary.multi.impl.MultiDictionaryActorProxyLike
import jp.o3co.config.Settings
import java.util.Locale

import store.TagDictionaryStoreAdapter

trait BaseTagDictionarySettings extends Settings {

  def key    = config.getConfig("key")

  def store  = config.getConfig("store")
}

case class LocalTagDictionarySettings(config: Config) extends BaseTagDictionarySettings {


  def isI18nSupport: Boolean = config.getOrElse("i18n_support", false)

  def defaultLocale: Locale = config.getOrElse[Locale]("default_locale", Locale.getDefault)
}

class LocalTagDictionaryActor(config: Config) extends Actor 
  with TagDictionaryActorLike
  with impl.TagDictionaryImpl
  with MultiDictionaryActorProxyLike[TagSegment, TagName, TagLabel]
{
  override val protocol = Protocol

  import protocol._

  val settings = LocalTagDictionarySettings(config)

  override val timeout: Timeout = settings.timeout

  implicit val executionContext = context.dispatcher

  val store = TagDictionaryStoreAdapter(settings.store)

  override val endpoint = store.endpoint

  override val generator = key.TagNameGenerator(settings.key, store)

  val defaultSegment: TagSegment = if(settings.isI18nSupport) {
    NamedTagSegment("default")
  } else {
    LocaleTagSegment(settings.defaultLocale)
  }

  def receive = receiveDictionaryCommand orElse receiveTagDictionaryCommand

}

object LocalTagDictionaryActor {
  def props(config: Config) = Props(classOf[LocalTagDictionaryActor], config)
}
