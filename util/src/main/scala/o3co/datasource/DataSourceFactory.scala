package o3co.datasource

import com.mchange.v2.c3p0.ComboPooledDataSource
import com.typesafe.config.Config
import com.typesafe.config.ImplicitConfigExtensions._
import javax.sql.DataSource

/**
 * DataSourceFactory from configuration
 */
trait DataSourceFactory {
  
  /**
   *
   */
  def apply(config: Config): DataSource
}

object ComboPooledDataSourceFactory extends DataSourceFactory{
  def apply(config: Config): DataSource = {
    val cpds = new ComboPooledDataSource()

    // 
    val dbtype = DBTypes(config.getString("type"))
    val path   = dbtype.createPath()
      .setHost(config.getOption[String]("host"))
      .setPort(config.getOption[Int]("port"))
      .setDbname(config.getString("dbname"))

    cpds.setJdbcUrl(path.toString)

    println(path.toString)

    (config.getOption[String]("username"), config.getOption[String]("password")) match {
      case (Some(u), Some(p)) =>
        cpds.setUser(u)
        cpds.setPassword(p)
      case (Some(u), None) => 
        cpds.setUser(u)
      case _ => 
    }

    // Optional settings
    cpds.setMinPoolSize(config.getOrElse[Int]("min-pool-size", 5))
    cpds.setAcquireIncrement(config.getOrElse[Int]("acquire-increment", 5))
    cpds.setMaxPoolSize(config.getOrElse[Int]("max-pool-size", 20))

    cpds
  }
}

case class DBPath(dbtype: DBType, host: Option[String] = Some("localhost"), port: Option[Int] = None, dbname: Option[String] = None) {

  def setHost(host: String) = copy(host = Some(host)) 

  def setHost(host: Option[String]) = copy(host = host.orElse(this.host))

  def setPort(port: Int) = copy(port = Some(port)) 

  def setPort(port: Option[Int]) = copy(port = port.orElse(this.port))

  def setDbname(dbname: String) = copy(dbname = Some(dbname)) 

  def setDbname(dbname: Option[String]) = copy(dbname = dbname.orElse(this.dbname))

  override def toString: String = 
    (dbtype, host, port, dbname) match {
      case (DBTypes.H2MEM, _, _, Some(n)) => 
        s"jdbc:h2:mem:${n}"
      case (t, Some(h), Some(p), Some(n)) => 
        s"jdbc:${t.name}://$h:$p/$n"
      case _ => 
        throw new IllegalArgumentException("Invalid Database Settings")
    }
}

case class DBType(name: String, defaultPort: Option[Int] = None) {

  def createPath(): DBPath = new DBPath(this, Some("localhost"), defaultPort, None)
}

object DBTypes {
  /**
   *
   */
  val MySQL     = DBType("mysql", Some(3306))
  val Postgres  = DBType("posgresql", Some(5432))
  val H2TCP     = DBType("h2:tcp", Some(9092))
  val H2MEM     = DBType("h2:mem")

  def apply(name: String) = name.toLowerCase match {
    case "mysql" => MySQL
    case "postgresql" => Postgres 
    case "h2:tcp" => H2TCP 
    case "h2:mem" => H2MEM 
  }
}
