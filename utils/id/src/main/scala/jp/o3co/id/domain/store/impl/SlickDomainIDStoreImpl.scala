package jp.o3co.id.domain.store
package impl

import jp.o3co.net.DomainName
import scala.concurrent.ExecutionContext
import slick.driver.JdbcProfile

/**
 *
 */
trait SlickDomainIDStoreImpl[ID] extends DomainIDStoreImpl[ID] {
  
  val profile: JdbcProfile
  import profile.ColumnType
  import profile.api._

  def database: Database

  implicit def executionContext: ExecutionContext 

  implicit def idColumnType: ColumnType[ID]

  implicit def domainNameColumnType = MappedColumnType.base[DomainName, String](_.toString, DomainName(_)(true))

  class DomainIds(tag: Tag) extends Table[(DomainName, ID)](tag, "domain_ids") {
    def domain  = column[DomainName]("domain")
    def id      = column[ID]("id")

    def * = (domain, id)

    def pk = primaryKey("pk_domain_id", (domain, id))
  }

  val domainIds = TableQuery[DomainIds]

  def existsAsync(domain: DomainName, id: ID) = {
    database.run(domainIds.filter(row => (row.domain === domain) && (row.id === id)).length.result)
      .map(s => s > 0)
  }

  def putAsync(domain: DomainName, id: ID) = {
    database.run(domainIds += (domain, id))
      .map(_ => (): Unit)
  }

  /**
   * Delete domain and subdomain related id
   */
  def deleteAsync(domain: Option[DomainName], id: ID) = {
    (domain match {
      case Some(d) => database.run(domainIds.filter(row => ((row.domain ===  d) || (row.domain.asColumnOf[String] like s"${d.name}.%")) && (row.id === id)).delete)
      case None    => database.run(domainIds.filter(row => (row.id === id)).delete)
    })
      .map(_ => (): Unit)
  }
}
