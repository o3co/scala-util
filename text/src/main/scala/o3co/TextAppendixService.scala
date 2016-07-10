
case class FieldName(name: String) extends AnyVal {

}

case class Appendix(owner: AppendixOwnerID, field: FieldName, text: TextID)

trait TextAppendixService[AppendixOwnerID, TextID] {

  def existsAsync(owner: AppendixOwnerID, field: String): Future[Boolean]

  def getAsync(owner: AppendixOwnerID, field: String): Future[Option[String]]

  def findAsync(owner: AppendixOwnerID, prefix: String = "*"): Future[Map[FieldName, String]]

  def putAsync(owner: AppendixOwnerID, field: String, text: String): Future[Unit]

  def deleteAsync(owner: AppendixOwnerID, field: String, deleteText: Boolean = false): Future[Unit]

  def deleteAllByOnwerAsync(owner: AppendixOwnerID, prefix: String = "*", deleteText: Boolean = false): Future[Unit]

  def deleteAllByFieldAsync(field: String, deleteText: Boolean = false): Future[Unit]
}


// FIXME
trait StoredTextAppendixServiceImpl {

  def textStore: TextStoreService[TextID]

  def store: TextAppendixServiceStore[AppendixOwnerID, TextID]

  def existsAsync(owner: AppendixOwnerID, field: FieldName) = 
    store.existsByKeyAsync(owner, field)

  /**
   *
   * {{{
   *   tas.getAsync(contentId, "content.note")
   * }}}
   */
  def getTextAsync(owner: AppendixOwnerID, field: FieldName) = {

  }

  def findAsync(owner: AppendixOwnerID, prefix: FieldNamePrefix) = {
    store.getByOwnerAsync(owner).flatMap { fieldTexts => 
      val targets = fieldTexts.collect {
        case (f, t) if f.startsWith(field) => (f, t)
      }

      textStore.getKeyValuesAsync(targets.values).map { indexedTexts => 
        targets.collect {
          case (field, text) if indexedTexts.contains(text) => (field, indexedTexts(text))
        }
      }
    }
  }

  def putAsync(owner: AppendixOwnerID, field: FieldName, text: String) = {
    textStore.putAsync(text).flatMap { id => 
      store.putAsync(owner, field, id)
    }
  }

  /**
   * Delete appendix pair.
   *
   * {{{
   *   appendix.deleteAsync(contentId, "content.*", true)
   * }}}
   *
   * @param owner
   */
  def deleteAsync(owner: AppendixOwnerID, field: FieldName, deleteText: Boolean = false) = {
    if(deleteText) 
    store.getFieldTextAsync(owner, field).flatMap { fieldTexts => 
      // Delete text asynchronously
      if(deleteText) {
        textStore.deleteAsync(fieldTexts.values)
      }
      
      store.deleteAsync(owner, field)
    }
  }

  def deleteAllByOwnerAsync() = {
    
  }
}


trait TextAppendixServiceStore {
  def getFieldTextAsync(owner: AppendixOwnerID, field: FieldNamePrefix): Future[Map[FieldName, TextID]]
}
