package o3co
  
package object iterator {

  /**
   * 
   */
  implicit class NextOptionIterator[T](val underlying: Iterator[T]) extends AnyVal {
    /**
     * Get next with Option.
     * If exists, return Some. Otherwise return None
     */
    def nextOption(): Option[T]  = if(underlying.hasNext) Option(underlying.next()) else None
  
    /**
     * If iterator has no more next, then return given default 
     */
    def nextOrElse(default: T): T = nextOption.getOrElse(default)
  }
}
