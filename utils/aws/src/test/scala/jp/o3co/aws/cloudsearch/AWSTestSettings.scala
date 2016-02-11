package jp.o3co.aws
package cloudsearch

import jp.o3co.aws.config.AWSGlobalSettings
import jp.o3co.config.Settings

object AWSTestSettings extends Settings {

  lazy val config = AWSGlobalSettings.getConfig("test")

  lazy val isRemoteTestEnabled: Boolean = config.getOrElse("remote-test-enabled", false)
}
