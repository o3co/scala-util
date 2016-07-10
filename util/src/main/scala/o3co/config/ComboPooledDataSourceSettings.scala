package o3co.config

import com.mchange.v2.c3p0.ComboPooledDataSourceFactory

trait ComboPooledDataSourceSettingsLike extends JDBCSettingsLike {
  this: Settings => 

  def dataSource = ComboPooledDataSourceFactory(this)
}
