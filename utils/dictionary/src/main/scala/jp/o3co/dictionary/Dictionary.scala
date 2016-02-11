package jp.o3co.dictionary

/**
 *
 */
trait DictionaryComponents[K, V] {
  
  type TermKey = K

  type Term    = V
}

/**
 *
 */
trait BaseDictionary[K, V] extends DictionaryComponents[K, V] {

  /**
   *
   */
  type This = this.type
}

/**
 *
 */
trait Dictionary[K, V] extends BaseDictionary[K, V] {

  def contains(key: K): Boolean
  
  /**
   * Slow api.
   * Find all keys for the term
   */
  def keysOf(term: Term): Iterable[TermKey]

  /**
   * Get Term if exists
   */
  def getTerm(key: TermKey): Option[Term]

  /**
   *
   */
  def putTerm(key: TermKey, term: Term): Option[Term]

  /**
   *
   */
  def deleteTerm(key: TermKey): Option[Term]
}
