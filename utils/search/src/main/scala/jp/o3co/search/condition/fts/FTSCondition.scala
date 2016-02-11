package jp.o3co.search
package condition
package fts

/**
 * Base trait of Condition for FullTextSearch
 */
trait FTSCondition[T] extends Condition[T]

/**
 * Term search.
 */
case class ContainsTerm(value: String) extends FTSCondition[String]
