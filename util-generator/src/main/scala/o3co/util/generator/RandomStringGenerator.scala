package o3co.util.generator

import org.apache.commons.lang3.RandomStringUtils

/*
 *
 */
class RandomStringGenerator(val length: Int, override val validation: String => Boolean, override val numOfTries: Int) extends Generator[String] {

  def this(length: Int) = {
    this(length, { any => true }, 1)
  }

  override def doGenerate: String = RandomStringUtils.random(length)
}

object RandomStringGenerator {
  def apply(len: Int) = new RandomStringGenerator(len) 

  def apply(len: Int, f: String => Boolean, tries: Int = 1) = new RandomStringGenerator(len, f, tries) 
}
