package jp.o3co.aws
package config

import com.typesafe.config.Config
import jp.o3co.config.GlobalSettings
import jp.o3co.config.Settings


object AWSGlobalSettings extends Settings with AWSSettingsLike {
  
  lazy val config: Config = GlobalSettings.getConfig("aws")

  lazy val hasDefaultRegion: Boolean = hasRegion

  lazy val defaultRegion: Region = region

  lazy val hasDefaultCredentials: Boolean = hasCredentials

  lazy val defaultCredentials: Credentials = credentials
}

