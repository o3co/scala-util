package jp.o3co

import com.typesafe.config.Config

package object config extends Implicits {
  import scala.language.implicitConversions

  implicit def settingsToConfig(settings: Settings): Config = settings.config
}


