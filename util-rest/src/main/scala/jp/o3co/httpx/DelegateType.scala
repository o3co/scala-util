package jp.o3co.httpx

import scala.language.implicitConversions

case class DelegateType(_name: String) {
  /**
   *
   */
  override def equals(other: Any): Boolean = other match {
    case t: DelegateType => t.name == name
    case n: String => n.toLowerCase == name
    case _ => false
  }

  def name: String = _name.toLowerCase 

  override def toString: String = name
}

object DelegateType {
  trait Implicits {
    implicit def strToDelegateType(name: String) = DelegateType(name.toLowerCase)

    implicit def delegateTypeToStr(typed: DelegateType) = typed.name
  }

  object Implicits extends Implicits
}
