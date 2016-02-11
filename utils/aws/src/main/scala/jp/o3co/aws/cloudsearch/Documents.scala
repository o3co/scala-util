package jp.o3co.aws
package cloudsearch

import scala.collection.mutable

/**
 * Documents is Builder class for UploadDocument' fields.
 * There is 3 ways to specify Fields.
 *
 *   - From Document instance 
 *   - From Any object with Marshaller
 *   - From ID and Fields pair
 *
 * {{{
 *   val documents = Documents()
 *   documents
 *    .addDocument(AddDocument())
 *
 * }}}
 */
case class Documents(documents: mutable.Map[String, Document] = mutable.Map()) extends marshalling.DefaultMarshallers {
  type This = this.type

  def addDocument(document: AddDocument): This = 
    put(document)

  /**
   * Put AddDocument for an object
   */
  def addDocument[T](value: T)(implicit m: marshalling.DocumentMarshaller[T, AddDocument]): This = 
    addDocument(m(value).asInstanceOf[AddDocument])

  def addDocument(id: String, fields: FieldValues): This = 
    addDocument(AddDocument(id, fields.toJson))

  def deleteDocument(document: DeleteDocument): This = 
    put(document)

  /**
   * Put DeleteDocument for an object
   */
  def deleteDocument[T](value: T)(implicit m: marshalling.DocumentMarshaller[T, DeleteDocument]): This = 
    put(m(value))

  def deleteDocument(id: String): This = 
    put(DeleteDocument(id))

  def contains(id: String): Boolean = documents.contains(id)

  /**
   * Put Document into documents Map
   */
  def put(document: Document): This = {
    documents.put(document.id, document)
    this
  }

  /**
   * Delete id from documents Map
   */
  def remove(id: String): This = {
    documents.remove(id)
    this
  }

  /**
   * Documents Size
   */
  def size: Int = documents.size

  /**
   *
   */
  def toJson: String = documents.values.map(document => document.toJson).mkString("[", ",", "]")
}

sealed trait Document {
  /**
   * ID of target document
   */
  def id: String

  /**
   * Document Action type. Either Add or Delete
   */
  def action: DocumentAction

  /**
   * Convert to Json string
   * 
   */
  def toJson: String 
}

sealed abstract class AbstractDocument(val action: DocumentAction) extends Document

case class AddDocument(id: String, fields: String) extends AbstractDocument(DocumentAction.Add) {
  def toJson: String = s"""{"id":"$id","type":"add","fields":${fields}}"""
}

object AddDocument {
  /**
   * Create AddDocument with id and FieldValues
   */
  def apply(id: String, fields: FieldValues): AddDocument = AddDocument(id, fields.toJson)
}

case class DeleteDocument(id: String) extends AbstractDocument(DocumentAction.Delete) {
  def toJson = s"""{"id":"$id","type":"delete"}"""
}

trait DocumentAction
object DocumentAction {
  def apply(name: String) = name.toLowerCase match {
    case "add"    => Add
    case "delete" => Delete 
  }
  case object Add extends DocumentAction
  case object Delete extends DocumentAction
}
