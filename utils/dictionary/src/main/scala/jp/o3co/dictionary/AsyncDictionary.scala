package jp.o3co.dictionary

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

/**
 *
 */
trait AsyncDictionary[K, V] extends BaseDictionary[K, V] {

  def containsAsync(key: K): Future[Boolean]

  /**
   * Slow api.
   * Find all keys for the term
   */
  def keysOfAsync(term: Term): Future[Iterable[TermKey]]

  /**
   * Get Term if exists
   */
  def getTermAsync(key: TermKey): Future[Option[Term]]

  /**
   *
   */
  def putTermAsync(key: TermKey, term: Term): Future[Option[Term]]

  /**
   *
   */
  def deleteTermAsync(key: TermKey): Future[Option[Term]]
}

/**
 * Emulate Dictionary as an AsyncDictionary
 */
trait AsyncDictionaryLike[K, V] extends AsyncDictionary[K, V] {
  this: Dictionary[K, V] => 

  implicit def executionContext: ExecutionContext

  def containsAsync(key: K) = Future(contains(key))

  /**
   * Slow api.
   * Find all keys for the term
   */
  def keysOfAsync(term: Term) = Future(keysOf(term))

  /**
   * Get Term if exists
   */
  def getTermAsync(key: TermKey) = Future(getTerm(key))

  /**
   *
   */
  def putTermAsync(key: TermKey, term: Term) = Future(putTerm(key, term))

  /**
   *
   */
  def deleteTermAsync(key: TermKey) = Future(deleteTerm(key))
}
