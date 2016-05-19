package jp.o3co.search
package slick

//import _root_.slick.lifted.Rep
//import _root_.slick.lifted.AbstractTable
import _root_.slick.ast.BaseTypedType
import _root_.slick.ast.Ordering
import _root_.slick.driver.JdbcProfile
import _root_.slick.lifted.ColumnOrdered
import _root_.slick.lifted.Ordered
import jp.o3co.search.condition._
import scala.language.implicitConversions

trait SlickJDBCEnvironment {
  val profile: JdbcProfile
}

trait SlickExtensions extends SlickJDBCEnvironment{

  val profile: JdbcProfile
  import profile.api._

  implicit class OptionalConditionExtension[A: BaseTypedType](condition: Option[Condition[A]]) {
    
    def apply(column: Rep[A]): Rep[Boolean] = condition match {
      case Some(c) => c(column)
      case None    => true
    }
  }

  /**
   * Implicit class to append functionality to convert condition to query filter
   */
  implicit class SlickConditionExtension[A: BaseTypedType](condition: Condition[A]) {
    def apply(column: Rep[A]): Rep[Boolean] = condition match {
      case Equals(v)              => column === v
      case NotEquals(v)           => column =!= v
      case LessThan(v)            => column < v
      case LessThanOrEquals(v)    => column <= v
      case GreaterThan(v)         => column > v
      case GreaterThanOrEquals(v) => column >= v
      case InSet(v)               => column inSet v
      case And(conds@_*) => 
        conds.map(c => c(column)).reduceLeft((l, r) => (l && r))
      case Or(conds@_*) => 
        conds.map(c => c(column)).reduceLeft((l, r) => (l || r))
      case r: Range[A]            => 
        r.toComparisons(column)
    }
  }

  /**
   * Implicit conversion from OrdinalDirection to Slick Ordering
   */
  implicit def toSlickOrdering(direction: OrdinalDirection): Ordering = {
    direction match {
      case OrdinalDirections.ASC  => Ordering(Ordering.Asc)
      case OrdinalDirections.DESC => Ordering(Ordering.Desc)
    }
  }

  /**
   * Implicit conversion from OrderByFields to table Ordered 
   */
  implicit def toSlickTableOrdered[T <: Table[_]](orders: OrderByFields)(implicit f: T => OrderByField => Option[ColumnOrdered[_]]): T => Ordered = { row => 
    new Ordered(
      orders.fields
        .map(field => f(row)(field))
        .collect {
          case Some(o) => o.columns
        }
        .flatten
        .toIndexedSeq
    )
  }
}
