package o3co.store.slick

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import slick.driver.JdbcProfile
import slick.jdbc.meta.MTable

/**
 *
 */
trait SlickStoreLike {

  val profile: JdbcProfile
  import profile.api._

  def database: Database

  def tables: Seq[TableQuery[_ <: Table[_ <: Any]]]

  implicit def executionContext: ExecutionContext

  def createTables: Future[Unit] = {
    database.run(MTable.getTables).flatMap { tableMetas => 
      val defined = tableMetas.map(m => m.name.name)
      val tableSchemas = tables.collect {
          case t if(!defined.contains(t.baseTableRow.tableName)) => t.schema
        }

      if(tableSchemas.isEmpty) 
        Future.successful((): Unit)
      else 
        database.run(tableSchemas.reduce(_ ++ _).create)
    }
  }

  def dropTables: Future[Unit] = {
    database.run(MTable.getTables).flatMap { tableMetas => 
      val defined = tableMetas.map(tm => tm.name.name)

      val tableSchemas = tables.collect {
        case t if(defined.contains(t.baseTableRow.tableName)) => t.schema
      }

      if(tableSchemas.isEmpty) 
        Future.successful((): Unit)
      else 
        database.run(tableSchemas.reduce(_ ++ _).drop)
    }
  }

  def dropAndCreateTables: Future[Unit] = {

    database.run(MTable.getTables).flatMap { tableMetas => 
      val defined = tableMetas.map(tm => tm.name.name)

      val existsSchemas = tables.collect {
        case t if(defined.contains(t.baseTableRow.tableName)) => t.schema
      }
      
      database.run(if(existsSchemas.nonEmpty) {
        DBIO.seq(
          existsSchemas.reduce(_ ++ _).drop,
          tables.map(_.schema).reduce(_ ++ _).create
        ).transactionally
      } else {
        tables.map(_.schema).reduce(_ ++ _).create
      })
    }
  }
}

