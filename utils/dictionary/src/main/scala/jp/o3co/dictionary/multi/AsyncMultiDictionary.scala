package jp.o3co.dictionary
package multi

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

/**
 *
 */
trait AsyncMultiDictionary[A, K, V] extends BaseMultiDictionary[A, K, V] {

  type SubDictionary = AsyncDictionary[K, V]
  
  def dictionary(selector: A): SubDictionary = getDictionary(selector).getOrElse(throw new NoSuchElementException("Not found key: " + selector))

  def getDictionary(selector: A): Option[SubDictionary]

  def containsAsync(key: K, selector: Option[A]): Future[Boolean]

  /**
   * Slow api.
   * Find all keys for the term
   */
  def keysOfAsync(term: Term, selector: A): Future[Iterable[TermKey]]

  /**
   * Get Term if exists
   */
  def getTermAsync(key: TermKey, selector: A): Future[Option[Term]]

  /**
   *
   */
  def putTermAsync(key: TermKey, term: Term, selector: A): Future[Option[Term]]

  /**
   *
   */
  def deleteTermAsync(key: TermKey, selector: A): Future[Option[Term]]
}

/**
 * Emulate Dictionary as an AsyncDictionary
 */
trait AsyncMultiDictionaryLike[A, K, V] extends AsyncMultiDictionary[A, K, V] {
  this: MultiDictionary[A, K, V] => 

  implicit def executionContext: ExecutionContext

  /**
   * Slow api.
   * Find all keys for the term
   */
  def keysOfAsync(term: Term, selector: A) = Future(keysOf(term, selector))

  /**
   * Get Term if exists
   */
  def getTermAsync(key: TermKey, selector: A) = Future(getTerm(key, selector))

  /**
   *
   */
  def putTermAsync(key: TermKey, term: Term, selector: A) = Future(putTerm(key, term, selector))

  /**
   *
   */
  def deleteTermAsync(key: TermKey, selector: A) = Future(deleteTerm(key, selector))
}
