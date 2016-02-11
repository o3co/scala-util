package jp.o3co.generator
package impl

import java.util.UUID

/*
 *
 */
trait RandomUUIDGenerator extends Generator[UUID] {
  def generate: UUID = UUID.randomUUID()
}

object RandomUUIDGenerator extends BaseImplicitConversions {

  def apply(): RandomUUIDGenerator = new RandomUUIDGenerator {}
}

