package jp.o3co.id.domain.store
package impl

import java.util.UUID

/**
 * Sample UUID DomainIDStore
 */
trait UUIDSlickDomainIDStore extends DomainIDStore[UUID] with SlickDomainIDStoreImpl[UUID] {

  /**
   *
   */
  val idColumnType = profile.columnTypes.uuidJdbcType
}


