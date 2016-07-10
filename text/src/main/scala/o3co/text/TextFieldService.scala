

trait TextFieldStore[ID] {
  
  def putFieldAsync(id: ID, field: String, value: String): Future[Unit]

}

trait TextFieldStoreImpl {
  
  def underlying: KeyValueStore[(ID, Domain), String]

  def putAsync() = {
    putAsync((id, Domain(field, false)), value)
  }
}


