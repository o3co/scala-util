package jp.o3co.search.condition

import jp.o3co.matcher.{Matcher, Matchers}
import java.lang.Comparable

/**
 * import DefaultToMatchers._
 */
object DefaultToMatchers {

  class ComparableConditionToMatchers[T <% Comparable[T]] {
    import scala.language.implicitConversions

    implicit def eqToMatcher(condition: Equals[T]): Matcher[T]              = Matchers.Equals[T](condition.value)
    implicit def neToMatcher(condition: NotEquals[T]): Matcher[T]           = Matchers.NotEquals[T](condition.value)
    implicit def ltToMatcher(condition: LessThan[T]): Matcher[T]            = Matchers.LessThan[T](condition.value)
    implicit def leToMatcher(condition: LessThanOrEquals[T]): Matcher[T]    = Matchers.LessThanOrEquals[T](condition.value)
    implicit def gtToMatcher(condition: GreaterThan[T]): Matcher[T]         = Matchers.GreaterThan[T](condition.value)
    implicit def geToMatcher(condition: GreaterThanOrEquals[T]): Matcher[T] = Matchers.GreaterThanOrEquals[T](condition.value)
    implicit def rangeToMatcher(condition: Range[T]): Matcher[T]            = (condition.min, condition.max) match {
      case (Some(min), Some(max)) => Matchers.Between[T](min, max, condition.includeMin, condition.includeMax)
      case (Some(min), None) => 
        if(condition.includeMin) Matchers.GreaterThanOrEquals[T](min)
        else Matchers.GreaterThan(min)
      case (None, Some(max)) => 
        if(condition.includeMax) Matchers.LessThanOrEquals[T](max)
        else Matchers.LessThan(max)
      case (None, None)      => throw new IllegalStateException("Range has to have either min or max.")
    }

    implicit def inToMatcher(condition: InSet[T]): Matcher[T]                  = Matchers.InSet[T](condition.values)
    //implicit def comparableToMatchers(condition: Condition[T]): Matcher[T] = condition match {
    //  case Equals(v)    => Matchers.Equals[T](v)
    //  case NotEquals(v) => Matchers.NotEquals[T](v)
    //}
  }

  trait StringConditionToMatcher extends ComparableConditionToMatchers[String]
  trait IntConditionToMatcher extends ComparableConditionToMatchers[Int]


  trait ListConditionToMatchers[T] {
    import scala.language.implicitConversions
    implicit def eqToMatcher(condition: Equals[T]): Matcher[T]    = Matchers.Equals[T](condition.value)
    implicit def neToMatcher(condition: NotEquals[T]): Matcher[T] = Matchers.NotEquals[T](condition.value)
  }

  trait SeqToMatcher[T] extends ListConditionToMatchers[Seq[T]] 
  trait SetToMatcher[T] extends ListConditionToMatchers[Set[T]] 
  //val stringConditionToMatchers = new ComparableToMatchers[String] 
  //val numericConditionToMatchers = new ComparableToMatchers[] 
  //val intConditionToMatchers = new ComparableToMatchers[Int]
  //val doubleConditionToMatchers = new ComparableToMatchers[Int]
  //val floatConditionToMatchers = new ComparableToMatchers[Int]
}
//trait ComparableToMatcher[T <: Comparable] {
//  
//}
///**
// * ToMatchers trait to convert Condition to Matchers implicitly.
// *
// * {{{
// *   val condition = Equals(some)
// *
// *   val bool: Boolean  = condition.matches(any)
// * }}}
// */
//trait ToMatchers {
//  import scala.language.implicitConversions
//
//  implicit def eqToMatcher[T](condition: Equals[T])              = Matchers.Equals[T](condition.value)
//  implicit def neToMatcher[T](condition: NotEquals[T])           = Matchers.NotEquals[T](condition.value)
//  implicit def ltToMatcher[T <: Comparable[T]](condition: LessThan[T])            = Matchers.LessThan[T](condition.value)
//  implicit def leToMatcher[T <: Comparable[T]](condition: LessThanOrEquals[T])    = Matchers.LessThanOrEquals[T](condition.value)
//  implicit def gtToMatcher[T <: Comparable[T]](condition: GreaterThan[T])         = Matchers.GreaterThan[T](condition.value)
//  implicit def geToMatcher[T <: Comparable[T]](condition: GreaterThanOrEquals[T]) = Matchers.GreaterThanOrEquals[T](condition.value)
//
//  implicit def rangeToMatcher[T <: Comparable[T]](condition: Range[T]) = (condition.min, condition.max) match {
//    case (Some(min), Some(max)) => Matchers.Between[T](min, max, condition.includeMin, condition.includeMax)
//    case (Some(min), None)      => 
//      if(condition.includeMin)  Matchers.GreaterThanOrEquals(min)
//      else                      Matchers.GreaterThan(min)
//    case (None, Some(max))      => 
//      if(condition.includeMax)  Matchers.LessThanOrEquals(max)
//      else                      Matchers.LessThan(max)
//  }
//  implicit def inToMatcher[T](condition: In[T])       = Matchers.InSet[T](condition.values)
//
//  implicit def andToMatcher[T](condition: And[T])     = Matchers.And[T](condition.conds.map(c => c: Matcher[T]): _*)
//  implicit def orToMatcher[T](condition: Or[T])       = Matchers.Or[T](condition.conds.map(c => c: Matcher[T]): _*)
//
//  implicit def containsToSeqMatcher[T](condition: Contains[T]): Matcher[Seq[T]] = Matchers.Contains(condition.value)
//  implicit def containsToSetMatcher[T](condition: Contains[T]): Matcher[Set[T]] = Matchers.SetContains(condition.value)
//}
//object ToMatchers extends ToMatchers
