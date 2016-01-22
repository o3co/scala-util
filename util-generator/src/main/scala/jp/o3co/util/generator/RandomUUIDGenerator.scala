package jp.o3co.util.generator

import java.util.UUID

/*
 *
 */
trait RandomUUIDGenerator extends Generator[UUID] {

  override def doGenerate: UUID = UUID.randomUUID()
}

object RandomUUIDGenerator {

  def apply(): RandomUUIDGenerator = new RandomUUIDGenerator {}

  def apply(f: UUID => Boolean, tries: Int = 1) = new RandomUUIDGenerator {
    override lazy val validation = f
    override val numOfTries = tries
  }
}

