package jp.o3co

import jp.o3co.dictionary._

package object sample extends DictionaryComponents[String] {

  case class SampleTermKey(key: String, segment: String) extends TermKey {
    override def toString: String = (segment, key).mkString(":")
  }

  type TermKey  = SampleTermKey
}
