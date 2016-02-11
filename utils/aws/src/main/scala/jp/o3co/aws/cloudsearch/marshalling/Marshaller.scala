package jp.o3co.aws
package cloudsearch
package marshalling

/**
 * DocumentMarshaller to convert from Object to Document to Document..
 * Either ToDocumentMarshaller or ToAdd/ToDeleteDocumentMarshaller will solve
 * the conversion from Object to Document.
 *
 */
trait DocumentMarshaller[-A, -B <: Document] extends FieldValueConversions {
  def apply(value: A): Document 
}

/**
 * DocumentMarshaller for T which convert to either AddDocument or DeleteDocument.
 * Commonly used for Model to Document conversion where model has the status to decide add/delete.
 */
trait ToDocumentMarshaller[T] extends DocumentMarshaller[T, Document] {
  def apply(value: T): Document
}

object ToDocumentMarshaller {
  def apply[T](f: T => Document) = new ToDocumentMarshaller[T] {
    def apply(value: T): Document = f(value)
  }
}

/**
 * DocumentMarshaller for conversion from T to AddDocument
 */
trait ToAddDocumentMarshaller[T] extends DocumentMarshaller[T, AddDocument] {
  def apply(value: T): Document
}

object ToAddDocumentMarshaller {
  def apply[T](f: T => AddDocument) = new ToAddDocumentMarshaller[T] {
    def apply(value: T): Document = f(value)
  }
}

/**
 * DocumentMarshaller for conversion from T to DeleteDocument.
 */
trait ToDeleteDocumentMarshaller[T] extends DocumentMarshaller[T, DeleteDocument] {
  def apply(value: T): Document
}

object ToDeleteDocumentMarshaller {
  def apply[T](f: T => DeleteDocument) = new ToDeleteDocumentMarshaller[T] {
    def apply(value: T): Document = f(value)
  }
}

trait DefaultMarshallers {
  import scala.language.implicitConversions

  implicit def numericToDeleteMarshaller[T: Numeric](value: T): DeleteDocument = DeleteDocument(value.toString)
  
  implicit def functionToMarshaller[A](f: A => Document): ToDocumentMarshaller[A] = new ToDocumentMarshaller[A] {
    def apply(value: A): Document = f(value)
  }

  implicit def functionToDeleteMarshaller[A](f: A => DeleteDocument): ToDeleteDocumentMarshaller[A] = new ToDeleteDocumentMarshaller[A] {
    def apply(value: A): Document = f(value)
  }

  implicit def functionToAddMarshaller[A](f: A => AddDocument): ToAddDocumentMarshaller[A] = new ToAddDocumentMarshaller[A] {
    def apply(value: A): Document = f(value)
  }

  /**
   * Implicit conversion from any to AddDocument with DocumentMarshaller[T, AddDocument]
   */
  implicit def anyToAddDocument[T](value: T)(implicit m: DocumentMarshaller[T, AddDocument]): AddDocument = m(value).asInstanceOf[AddDocument]

  /**
   * Implicit conversion from any to DeleteDocument with DocumentMarshaller[T, DeleteDocument]
   */
  implicit def anyToDeleteDocument[T](value: T)(implicit m: DocumentMarshaller[T, DeleteDocument]): DeleteDocument = m(value).asInstanceOf[DeleteDocument]
}

object DefaultMarshallers extends DefaultMarshallers

