package jp.o3co.tag

package object filter {
  /**
   * TagFilter is a filter to convert from TagName to Any 
   */
  type TagNameFilter[T]        = Filter[TagNameSet, Set[T]]

  /**
   * ReverseTagFilter is a filter to convert from any to TagName
   * Provide such input to tagName conversion
   * Output is not TagName, and you need .asTagName or implicitly convert from Tag to TagName when TagName is needed. 
   */
  type ReverseTagNameFilter[T] = Filter[Set[T], TagNameSet]

  /**
   * Filter to convert from Tag to Any
   */
  type TagFilter[T]  = Filter[TagSet, Set[T]] 
  
  object TagFilter {
    /**
     * Factory method to create TagFilter with Function1[TagSet, T]
     */
    def apply[T](f: Tag => T) = new TagFilter[T] {
      def filter(values: TagSet): Set[T] = values.map(value => f(value))
    }
  }
  
  /**
   * Filter to convert from Any to Tag 
   */
  type ReverseTagFilter[T] = Filter[Set[T], TagSet]
  
  object ReverseTagFilter {
    /**
     * Factory method to create ReverseTagFilter with Function1[T, TagSet]
     */
    def apply[T](f: T => Tag) = new ReverseTagFilter[T] {
      def filter(values: Set[T]): TagSet = values.map(value => f(value))
    }
  }
}


