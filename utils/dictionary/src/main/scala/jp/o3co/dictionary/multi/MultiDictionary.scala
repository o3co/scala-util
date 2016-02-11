package jp.o3co.dictionary
package multi

/*
 *
 */
trait BaseMultiDictionary[A, K, V] extends BaseDictionary[K, V] {
  
  type SubDictionary <: BaseDictionary[K, V]

  def getDictionary(selector: A): Option[SubDictionary]
}

/**
 *
 */
trait MultiDictionary[A, K, V] extends BaseMultiDictionary[A, K, V] {

  type SubDictionary = Dictionary[K, V]

  def dictionary(selector: A): SubDictionary = getDictionary(selector).getOrElse(throw new NoSuchElementException("Not found key: " + selector))

  def getDictionary(selector: A): Option[SubDictionary]

  def contains(key: K, selector: Option[A]): Boolean

  def keysOf(term: V, selector: A): Iterable[K]

  def getTerm(key: K, selector: A): Option[V]

  def putTerm(key: K, term: V, selector: A): Option[V]

  def deleteTerm(key: K, selector: A): Option[V]
}

/**
 *
 */
trait AutoExpandableMultiDictionaryLike[A, K, V] {
  this: BaseMultiDictionary[A, K, V] => 

  var valueFactory: Option[A => SubDictionary] = None

  /**
   * Expand MultiDictionary with new dictionary.
   * If this is a Collection of dictionary, then need to put the newDictinary for the selector.
   */
  protected def expandWith(selector: A, newDictionary: SubDictionary): Unit = {}

  /**
   * Select a dictionary for key or if not exists, then create new Dictionary for the key.
   * Not like "Map.withDefault", this increment the number of map element as well 
   *
   * @param selector
   */
  def dictionary(selector: A): SubDictionary = getDictionary(selector) match {
    case None    => {
      val newDictionary = valueFactory match {
        case Some(f) => f(selector)
        case None    => throw new NoSuchElementException("key not found: " + selector)
      }
      expandWith(selector, newDictionary)
      newDictionary
    }
    case Some(x) => x
  }

  /**
   *
   */
  def withValueFactory(f: A => SubDictionary): this.type = {
    valueFactory = Option(f)
    this
  }
}
