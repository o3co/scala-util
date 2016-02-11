package jp.o3co.aws
package cloudsearch
package marshalling

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import jp.o3co.config._


class MarshallerSpec extends Specification {
  trait Foo {
    def toValue: Int 
  }
  case class Bar(value: Int) extends Foo {
    def toValue: Int = value
  }

  "ToDocumentMarshaller[A]" should {
    trait WithFooMarshaller extends Scope {
      implicit object MyMarshaller extends ToDocumentMarshaller[Foo] {
        def apply(foo: Foo) = foo.toValue match {
          case 0 => DeleteDocument("id")
          case _ => AddDocument("id", "")
        }
      }
    }
    trait WithBarMarshaller extends Scope {
      implicit object MyMarshaller extends ToDocumentMarshaller[Bar] {
        def apply(bar: Bar) = bar.value match {
          case 0 => DeleteDocument("id")
          case _ => AddDocument("id", "")
        }
      }
    }
    "be implicit of ToAdd/DeleteDocumentMarshaller" in new WithFooMarshaller {
      implicitly[DocumentMarshaller[Bar, AddDocument]] === MyMarshaller
      implicitly[DocumentMarshaller[Bar, DeleteDocument]] === MyMarshaller
    }
    "be implicit of ToAdd/DeleteDocumentMarshaller" in new WithBarMarshaller {
      implicitly[DocumentMarshaller[Bar, AddDocument]] === MyMarshaller
      implicitly[DocumentMarshaller[Bar, DeleteDocument]] === MyMarshaller
    }
  }

  "ToAddDocumentMarshaller[A]" should {
    implicit object MyMarshaller extends ToAddDocumentMarshaller[Bar] {
      def apply(bar: Bar) = bar.value match {
        case _ => AddDocument("id", "")
      }
    }

    "be implicit of ToAddDocumentMarshaller" in {
      implicitly[DocumentMarshaller[Bar, AddDocument]] === MyMarshaller
    }
  }

  "ToDeleteDocumentMarshaller[A]" should {
    implicit object MyMarshaller extends ToDeleteDocumentMarshaller[Bar] {
      def apply(bar: Bar) = bar.value match {
        case _ => DeleteDocument("id")
      }
    }
    "be implicit of ToDeleteDocumentMarshaller" in {
      implicitly[DocumentMarshaller[Bar, DeleteDocument]] === MyMarshaller
    }
    // Compile Error
    //"be not implicit of ToAddDocumentMarshaller" in {
    //  implicitly[DocumentMarshaller[Bar, AddDocument]] === MyMarshaller
    //}
  }

  "DefaultMarshallers" should {
    import DefaultMarshallers._ 

    "convert function Any => Document to ToDocumentMarshaller" in {
      val f = { a: String => 
        a match {
          case "add"    => AddDocument("bar", "")
          case "delete" => DeleteDocument("bar")
        }
      }
      val doc: ToDocumentMarshaller[String] = f 
      //// Compiler Error: Correct
      //val add: ToAddDocumentMarshaller[String] = f
      //val del: ToDeleteDocumentMarshaller[String] = f

      doc must beAnInstanceOf[ToDocumentMarshaller[String]]
    }
    "convert function Any => Document to ToDocumentMarshaller" in {
      val f = { a: String => 
        AddDocument("bar", "")
      }
      val add: ToAddDocumentMarshaller[String] = f
      //// Compiler Error: Correct
      //val both: ToDocumentMarshaller[String] = f
      //val del: ToDeleteDocumentMarshaller[String] = f

      add must beAnInstanceOf[ToAddDocumentMarshaller[String]]
    }
  }
}
