package jp.o3co.util
package json

import scala.reflect.runtime.universe._
import scala.reflect.runtime.currentMirror
import org.json4s._
import org.json4s.Extraction.{decompose, extract}

object BoxSerializer {
  def apply[A <: Box[B] :TypeTag, B: TypeTag]() = new BoxSerializer[A, B]()
}

class BoxSerializer[A <: Box[B]: TypeTag, B: TypeTag] extends Serializer[A] {

  private val tTag  = implicitly[TypeTag[A]]
  private val clazz = tTag.mirror.runtimeClass(tTag.tpe)

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), A] = {
    case (TypeInfo(clazz, _), json) => json match {
      case JNull | JNothing => throw new Exception("")
      case other => 
        val pType = implicitly[TypeTag[B]].tpe
        val pClass = currentMirror.runtimeClass(pType) 
        val raw = extract(other, TypeInfo(currentMirror.runtimeClass(pType), None))

        currentMirror.reflectClass(tTag.tpe.typeSymbol.asClass).reflectConstructor(
          tTag.tpe.decl(termNames.CONSTRUCTOR).asMethod
        )(raw).asInstanceOf[A]
    }
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case value: Box[_] => Extraction.decompose(value.unwrapped)
  }
}
