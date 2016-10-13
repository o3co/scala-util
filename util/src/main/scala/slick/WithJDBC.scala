package slick

import slick.driver.JdbcProfile

/**
 * With JDBC Profile
 */
trait WithJDBC {

  val profile: JdbcProfile
  import profile.api.Database

  def database: Database
}
