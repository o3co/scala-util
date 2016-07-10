package jp.o3co.config
package jdbc

import com.typesafe.config.Config

trait DBType
object DBType {
  def apply(name: String) = {
    name.toLowerCase match {
      case "mysql"     => DBTypes.MySQL
      case "postgre"   => DBTypes.PostgreSQL
      case "h2mem"     => DBTypes.H2Mem
      case "h2tcp"     => DBTypes.H2TCP
      case "h2"        => DBTypes.H2Mem
    }
  }
}
object DBTypes {
  case object MySQL extends DBType 
  case object PostgreSQL extends DBType 
  case object H2TCP extends DBType 
  case object H2Mem extends DBType 
}

trait JDBCSettings extends Settings {
  
  def username: Option[String] = config.getOption[String]("username")

  def password: Option[String] = config.getOption[String]("password")

  /**
   *
   */
  def driverClass: String = {
    dbType match {
      case DBTypes.H2Mem | DBTypes.H2TCP => "org.h2.Driver"
      case DBTypes.MySQL      => "com.mysql.jdbc.Driver" 
      case DBTypes.PostgreSQL => "org.postgresql.Driver" 
      case _ => throw new Exception("Not supported.")
    }
  }

  /**
   *
   */
  def dbType: DBType = DBType(config.get[String]("type")) 

  def host: String = config.get[String]("host")

  def port: Int = config.getOrElse[Int]("port", dbType match {
    case DBTypes.MySQL      => 3306 
    case DBTypes.PostgreSQL => 5432
    case DBTypes.H2TCP      => 9092 
    case DBTypes.H2Mem      => 0 
    case _          => throw new Exception("Unknwon DBType for default Port Number.")
  })

  def dbname: String = config.get[String]("dbname")

  /**
   *
   */
  def jdbcUrl: String = dbType match {
    case DBTypes.H2Mem => 
      s"jdbc:h2:mem:$dbname"
    case DBTypes.H2TCP => 
      s"jdbc:h2:tcp://$host:$port/$dbname"
    case DBTypes.MySQL  => 
      s"jdbc:mysql://$host:$port/$dbname"
    case DBTypes.PostgreSQL => 
      s"jdbc:postgresql://$host:$port/$dbname"
    case _ => 
      throw new Exception("Unknown DBType for JDBCUrl")
  }
}

object JDBCSettings {
  def apply(c: Config) = new JDBCSettings {
    override val config = c
  }
}
