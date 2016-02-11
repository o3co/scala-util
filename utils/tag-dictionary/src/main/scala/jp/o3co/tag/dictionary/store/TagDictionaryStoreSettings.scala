package jp.o3co.tag
package dictionary
package store

import com.typesafe.config.Config
import javax.sql.DataSource
import jp.o3co.config.jdbc.ComboPooledDataSource
import jp.o3co.config.jdbc.JDBCSettings

/**
 *
 */
case class TagDictionaryStoreSettings(override val config: Config) extends JDBCSettings {

  val dataSource: DataSource = ComboPooledDataSource(this)
}
