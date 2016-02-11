package jp.o3co.aws
package cloudsearch
package query

/**
 * Base trait of Query
 */
trait Query {
  def query: String

  def parser: QueryParser

  def options: QueryOptions = QueryOptions()

  override def toString: String = query

  def toRequest: SearchRequest = SearchRequest(this)
}

/**
 *
 */
sealed abstract class AbstractQuery(val parser: QueryParser) extends Query

/**
 *
 */
case class SimpleQuery(query: String, override val options: QueryOptions = QueryOptions()) extends AbstractQuery(QueryParsers.Simple)

/**
 *
 */
case class LuceneQuery(query: String, override val options: QueryOptions = QueryOptions()) extends AbstractQuery(QueryParsers.Lucene)

/**
 *
 */
case class StructuredQuery(query: String, override val options: QueryOptions = QueryOptions()) extends AbstractQuery(QueryParsers.Structured)

