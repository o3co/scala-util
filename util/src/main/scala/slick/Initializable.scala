package slick

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import slick.driver.JdbcProfile
import slick.jdbc.meta.MTable

/**
 * To initialize database
 */
trait Initializable extends WithJDBC {

  import profile.api._

  def tables: Seq[TableQuery[_ <: Table[_ <: Any]]]

  implicit def executionContext: ExecutionContext

  /**
   * Create Tables
   */
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

  /**
   * DropTables
   */
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
    for {
      _ <- dropTables
      _ <- createTables
    } yield ((): Unit)
  }
}
