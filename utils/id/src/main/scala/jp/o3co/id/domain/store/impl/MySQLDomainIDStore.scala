package jp.o3co.id.domain.store
package impl

import jp.o3co.net.DomainName

trait MySQLDomainIDStoreImpl[ID] extends SlickDomainIDStoreImpl[ID] {

  val profile = slick.driver.MySQLDriver

  import profile.api._

}
