package o3co.search
package fts
package conditions

/**
 * Term search.
 */
case class ContainsTerm(value: String) extends FtsCondition[String]

