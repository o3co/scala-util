package jp.o3co.util.generator

import org.apache.commons.lang3.RandomStringUtils

/*
 *
 */
trait RandomStringGenerator extends Generator[String] {
  def length: Int

  override def doGenerate: String = RandomStringUtils.randomAlphanumeric(length)
}

object RandomStringGenerator {
  def apply(len: Int) = new RandomStringGenerator {
    override val length = len
  }

  def apply(len: Int, f: String => Boolean, tries: Int = 1) = new RandomStringGenerator {
    override val length = len
    override lazy val validation = f
    override val numOfTries = tries
  }
}
