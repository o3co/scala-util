package slick

import slick.ast.BaseTypedType
import slick.ast.Ordering
import slick.driver.JdbcProfile
import slick.lifted.ColumnOrdered
import slick.lifted.Ordered
import o3co.search.conditions._
import scala.language.implicitConversions
import o3co.search._

trait SlickJDBCEnvironment {
  val profile: JdbcProfile
}

trait SearchExtension extends SlickJDBCEnvironment {

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
      case Prefix(v)              => column.asColumnOf[String] like (v.toString + "%")
      case Suffix(v)              => column.asColumnOf[String] like ("%" + v.toString)
    }
  }

  implicit class OptionalOptionConditionExtension[A: BaseTypedType](condition: Option[Condition[Option[A]]]) {
    
    def apply(column: Rep[Option[A]]): Rep[Option[Boolean]] = condition match {
      case Some(c) => c(column)
      case None    => Option(true)
    }
  }

  /**
   * Implicit class to append functionality to convert condition to query filter
   */
  implicit class SlickOptionConditionExtension[A: BaseTypedType](condition: Condition[Option[A]]) {
    def apply(column: Rep[Option[A]]): Rep[Option[Boolean]] = condition match {
      case Equals(Some(v))              => column === v
      case Equals(None)                 => column.isEmpty.?  // isNull
      case NotEquals(Some(v))           => column =!= v
      case NotEquals(None)              => column.isDefined.? // isNotNull
      case LessThan(Some(v))            => column < v
      case LessThanOrEquals(Some(v))    => column <= v
      case GreaterThan(Some(v))         => column > v
      case GreaterThanOrEquals(Some(v)) => column >= v
      case InSet(v)                     => 
        (if(v.find(_.isEmpty).isDefined) {
          column.isEmpty 
        } else {
          true: Rep[Boolean]
        }) && (column inSet (
          v.collect {
            case Some(a) => a
          })
        )
      case And(conds@_*) => 
        conds.map(c => c(column)).reduceLeft((l, r) => (l && r))
      case Or(conds@_*) => 
        conds.map(c => c(column)).reduceLeft((l, r) => (l || r))
      case r: Range[Option[A]]            => 
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

