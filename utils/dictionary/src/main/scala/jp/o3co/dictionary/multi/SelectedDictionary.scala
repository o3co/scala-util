package jp.o3co.dictionary
package multi

/**
 * Base trait of both SelectedDictionary and AsyncSelectedDictionary.
 * 
 */
trait BaseSelectedDictionary[A, K, V] extends BaseDictionary[K, V] {

  type ParentDictionary <: BaseMultiDictionary[A, K, V]

  def underlying: ParentDictionary

  def selector: A
}

/**
 * SelectedDictionary is a dictionary which delegate methods to MultiDictionary with selector.
 */
trait SelectedDictionary[A, K, V] extends BaseSelectedDictionary[A, K, V] with Dictionary[K, V] {

  type ParentDictionary = MultiDictionary[A, K, V]

  def underlying: ParentDictionary

  /**
   * {@inheritDoc}
   */
  def contains(key: K) = underlying.dictionary(selector).contains(key)

  /**
   * {@inheritDoc}
   */
  def keysOf(term: V) = underlying.dictionary(selector).keysOf(term)

  /**
   * {@inheritDoc}
   */
  def getTerm(key: K) = underlying.dictionary(selector).getTerm(key)

  /**
   * {@inheritDoc}
   */
  def putTerm(key: K, term: V) = underlying.dictionary(selector).putTerm(key, term) 

  /**
   * {@inheritDoc}
   */
  def deleteTerm(key: K) = underlying.dictionary(selector).deleteTerm(key)
}

object SelectedDictionary {
  def apply[A, K, V](multi: MultiDictionary[A, K, V], s: A) = new SelectedDictionary[A, K, V] {
    override val underlying = multi    
    override val selector = s
  }
}

/*
 * AsyncSelectedDictionary is a dictionary which delegate methods to MultiDictionary with selector.
 */
trait AsyncSelectedDictionary[A, K, V] extends BaseSelectedDictionary[A, K, V] with AsyncDictionary[K, V] {

  type ParentDictionary = AsyncMultiDictionary[A, K, V]

  def underlying: ParentDictionary
  
  def containsAsync(key: K) = underlying.dictionary(selector).containsAsync(key)

  def keysOfAsync(term: V) = underlying.dictionary(selector).keysOfAsync(term)

  def getTermAsync(key: K) = underlying.dictionary(selector).getTermAsync(key)

  def putTermAsync(key: K, term: V) = underlying.dictionary(selector).putTermAsync(key, term)

  def deleteTermAsync(key: K) = underlying.dictionary(selector).deleteTermAsync(key)
}

object AsyncSelectedDictionary {
  def apply[A, K, V](multi: AsyncMultiDictionary[A, K, V], s: A) = new AsyncSelectedDictionary[A, K, V] {
    override val underlying = multi    
    override val selector = s
  }
}
