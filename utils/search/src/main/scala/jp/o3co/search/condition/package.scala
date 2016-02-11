package jp.o3co.search


/**
 * jp.o3co.search.condition Package.
 * Provide Condition and related to filter by each field condition.
 * This is commonly used on REST API to filter result. 
 */
package object condition {

  object Operators extends Enumeration {
    type Operator= Value
    val AND, OR, NOT, Equals, NotEquals, LessThan, LessThanOrEquals, GreaterThan, GreaterThanOrEquals = Value
  }
  type Operator = Operators.Value
}
