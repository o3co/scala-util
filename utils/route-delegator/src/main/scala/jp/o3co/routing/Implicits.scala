package jp.o3co.routing

import scala.language.implicitConversions

/**
 * Implicits behavior of httpx components
 */ 
trait Implicits {
  implicit def delegateTypeFromString(name: String) = DelegateType(name.toLowerCase)

  implicit def delegateTypeToString(typed: DelegateType) = typed.name
}

object Implicits extends Implicits 

