package jp.o3co

package object iterator {

  /**
   *
   */
  implicit class NextOptionIterator[T](underlying: Iterator[T]) {
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


