package jp.o3co.aws
package cloudsearch
package query

import com.amazonaws.services.cloudsearchdomain.{model => awsmodel}

/**
 *
 */
trait QueryParser

/**
 *
 */
object QueryParser {
  /**
   *
   */
  def apply(parser: awsmodel.QueryParser): QueryParser = apply(parser.toString)

  /**
   *
   */
  def apply(name: String): QueryParser = name.toLowerCase match {
    case "simple"     => QueryParsers.Simple
    case "lucene"     => QueryParsers.Lucene 
    case "structured" => QueryParsers.Structured 
    case "dismax"     => QueryParsers.Dismax 
  }
}

/**
 *
 */
object QueryParsers {
  case object Simple extends QueryParser
  case object Structured extends QueryParser
  case object Lucene extends QueryParser
  case object Dismax extends QueryParser
}
