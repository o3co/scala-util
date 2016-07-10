package jp.o3co.id.store
package impl

import java.util.UUID

/**
 * Sample extension with UUID ID Type
 */
trait UUIDSlickIDStore extends IDStore[UUID] with SlickIDStoreImpl[UUID] {

  /**
   *
   */
  val idColumnType = profile.columnTypes.uuidJdbcType
}

