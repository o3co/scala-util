package jp.o3co.util
package json

import org.specs2.mutable.Specification
import org.json4s._

case class StringBox(name: String) extends Box(name)

class BoxSerializerSpec extends Specification {
  
  "BoxSerializer" should {
    "serialize Box" in {
      val serializer = BoxSerializer[Box[Int], Int]
      val boxed = Box(20)
      implicit val formats: Formats = DefaultFormats

      serializer.serialize(formats)(boxed) === JInt(20)
    }
    "serialize Box-Ihnerited object" in {
      val serializer = BoxSerializer[StringBox, String]
      val boxed = StringBox("foo")
      implicit val formats: Formats = DefaultFormats

      serializer.serialize(formats)(boxed) === JString("foo")
    }
    "deserialize Box" in {
      val serializer = BoxSerializer[Box[Int], Int]
      implicit val formats: Formats = DefaultFormats

      serializer.deserialize(formats)(TypeInfo(classOf[Box[Int]], None), JInt(10)) === Box(10)
      serializer.deserialize(formats)(TypeInfo(classOf[Box[Int]], None), JInt(20)) !== Box(10)

      serializer.deserialize(formats)(TypeInfo(classOf[Box[Int]], None), JString("hoge")) must throwA[MappingException] 
    }
    "deserialize json for class inherited Box" in {
      val serializer = BoxSerializer[StringBox, String]
      implicit val formats: Formats = DefaultFormats

      serializer.deserialize(formats)(TypeInfo(classOf[StringBox], None), JString("foo")) === StringBox("foo")
    }
  }
}

