package jp.o3co.tag
package dictionary
package store

import akka.actor.Actor
import akka.actor.Props

class MySQLSlickActor(val settings: TagDictionaryStoreSettings) extends Actor with TagDictionaryStoreActorLike with SlickSQL {
  override val profile = slick.driver.MySQLDriver

  def receive = receiveStoreCommand
}

object MySQLSlickActor {
  def props(settings: TagDictionaryStoreSettings) = Props(classOf[MySQLSlickActor], settings)
}
