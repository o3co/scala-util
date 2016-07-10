package o3co.text.store


/**
 * {{{
 *   textStore.createAsync()
 * }}}
 */
trait TextStore[ID] extends KeyValueStore[ID, T] {
  
  def createAsync(text: String): Future[ID]

}
