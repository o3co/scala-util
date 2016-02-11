package jp.o3co.aws
package cloudsearch

import org.specs2.mutable.Specification
import jp.o3co.config._

import marshalling._

class DocumentsSpec extends Specification {
  
  "Documents" should {
    "put/remove documents" in {
      val documents = Documents()
      documents
        .put(AddDocument("foo", "{name: 'Foo'}"))
        .put(DeleteDocument("bar"))

      documents.size === 2
      documents
        .remove("bar")

      documents.size === 1
      documents.contains("foo") === true
    }

    "add/deleteDocument Any with DocumentMarshaller" in {

      case class Model(id: Int, name: String, status: String)

      implicit object modelToDocument extends ToDocumentMarshaller[Model] {
        def apply(model: Model): Document = {
          model.status match {
            case "deleted" => DeleteDocument(model.id.toString)
            case _ => AddDocument(model.id.toString, FieldValues(
              "status" -> model.status,
              "name"   -> model.name
            ))
          }
        }
      }

      val model = Model(1, "foo", "active") 
      val documents = Documents()
      import FieldValues._
      documents
        .addDocument(model)
        .addDocument("bar", FieldValues(
          "name" -> "bar",
          "status" -> "request"
        ))
        .deleteDocument("hoge")

      documents.size === 3

      documents.toJson === """[{"id":"1","type":"add","fields":{"status":"active","name":"foo"}},{"id":"hoge","type":"delete"},{"id":"bar","type":"add","fields":{"name":"bar","status":"request"}}]"""
    }
  }
}
