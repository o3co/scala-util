package jp.o3co.httpx

import scala.language.implicitConversions

/**
 * Implicits behavior of httpx components
 */ 
trait Implicits {
  implicit def delegateTypeFromString(name: String) = DelegateType(name.toLowerCase)

  implicit def delegateTypeToString(typed: DelegateType) = typed.name
}

object Implicits extends Implicits 

