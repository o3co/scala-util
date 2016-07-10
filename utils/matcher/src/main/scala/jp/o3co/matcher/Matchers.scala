package jp.o3co.matcher

import java.lang.Comparable
import shapeless._

/**
 * Default Matchers
 */
object Matchers {

  /**
   *
   */
  case class Equals[T](comparator: T) extends Matcher[T] {
    
    /**
     * {@ihneritDoc}
     */
    def matches(value: T) = comparator.equals(value) match {
      case true  => Matched(value :: HNil) 
      case false => Unmatched(s"$value is not equals $comparator")
    }

    def as[B](implicit f: T => B): Matcher[B] =
      Equals(f(comparator))
  }
  
  /**
   *
   */
  case class NotEquals[T](comparator: T) extends Matcher[T] {
    /**
     * {@ihneritDoc}
     */
    def matches(value: T) = comparator.equals(value) match {
      case true  => Unmatched(s"$value is equals $comparator")
      case false => Matched(value :: HNil) 
    }

    def as[B](implicit f: T => B): Matcher[B] = 
      NotEquals(f(comparator))
  }
  
  /**
   *
   */
  case class LessThan[T <% Comparable[T]](comparator: T) extends Matcher[T] {
    /**
     * {@ihneritDoc}
     */
    def matches(value: T) = comparator.compareTo(value) match {
      case n if n > 0 => Matched(value :: HNil)
      case _          => Unmatched(s"$value is not less than $comparator")
    }

    def as[B <% Comparable[B]](implicit f: T => B): Matcher[B] = 
      LessThan(f(comparator))
  }
  
  /**
   *
   */
  case class LessThanOrEquals[T <% Comparable[T]](comparator: T) extends Matcher[T] {
    /**
     * {@ihneritDoc}
     */
    def matches(value: T) = comparator.compareTo(value) match {
      case n if n >= 0 => Matched(value :: HNil)
      case _          => Unmatched(s"$value is not less than or eqauls $comparator")
    }

    def as[B <% Comparable[B]](implicit f: T => B): Matcher[B] = 
      LessThanOrEquals(f(comparator))
  }
  
  /**
   *
   */
  case class GreaterThan[T <% Comparable[T]](comparator: T) extends Matcher[T] {
    /**
     * {@ihneritDoc}
     */
    def matches(value: T) = comparator.compareTo(value) match {
      case n if n < 0 => Matched(value :: HNil)
      case _          => Unmatched(s"$value is not greater than $comparator")
    }

    def as[B <% Comparable[B]](implicit f: T => B): Matcher[B] = 
      GreaterThan(f(comparator))
  }
  
  /**
   *
   */
  case class GreaterThanOrEquals[T <% Comparable[T]](comparator: T) extends Matcher[T] {
    /**
     * {@ihneritDoc}
     */
    def matches(value: T) = comparator.compareTo(value) match {
      case n if n <= 0 => Matched(value :: HNil)
      case _          => Unmatched(s"$value is not greater than or eqauls $comparator")
    }

    def as[B <% Comparable[B]](implicit f: T => B): Matcher[B] = 
      GreaterThanOrEquals(f(comparator))
  }

  /**
   *
   */
  case class Between[T <% Comparable[T]](left: T, right: T, includeMin: Boolean = true, includeMax: Boolean = false) extends Matcher[T] {

    val (min, max) = if(left.compareTo(right) <= 0) (left, right) else (right, left)
    
    def matches(value: T) = ((includeMin, includeMax) match {
      case (true, true)   => (min.compareTo(value) <= 0) && (max.compareTo(value) >= 0)
      case (true, false)  => (min.compareTo(value) <= 0) && (max.compareTo(value) > 0)
      case (false, true)  => (min.compareTo(value) < 0) && (max.compareTo(value) >= 0)
      case (false, false) => (min.compareTo(value) < 0) && (max.compareTo(value) > 0)
    }) match {
      case true  => Matched(value :: HNil)
      case false => Unmatched(s"$value is not between $min and $max.")
    }

    def as[B <% Comparable[B]](implicit f: T => B): Matcher[B] = 
      Between(f(left), f(right), includeMin, includeMax)
  }

  case class InSet[T](comparators: Set[T]) extends Matcher[T]{
    def matches(value: T) = {
      if(comparators.contains(value)) Matched(value :: HNil)
      else Unmatched(s"$value is not in $comparators")
    }

    def as[B](implicit f: T => B): Matcher[B] = 
      InSet(comparators.map(f(_)))
  }
    
  trait Compound[T] extends Matcher[T] {
    def matchers: Seq[Matcher[T]]
  }

  case class And[T](matchers: Matcher[T] *) extends Matcher[T] {
    case class UnmatchedTermination(message: String) extends Throwable
    /**
     *
     */
    def matches(value: T) = {
      try {
        Matched(matchers
          // Get all matched condition
          .map { matcher => 
            matcher.matches(value) match {
              case Matched(matched) => matched
              case Unmatched(cause) => throw new UnmatchedTermination(cause)
            }
          }
          .reduce(_::_)
        )
      } catch {
        case UnmatchedTermination(message) => Unmatched(message)
      }
    }

    //def as[B](implicit f: T => B): Matcher[B] = 
    //  And[B](matchers.map(m => m.as[B]):_*)
  }
  
  case class Or[T](matchers: Matcher[T] *) extends Matcher[T] {
    
    def matches(value: T): Matching = {
      matchers
        // Convert all matcher to the result
        .map(matcher => matcher.matches(value))
        .collectFirst {
          case x: Matched => x
        }
        .getOrElse(Unmatched(s"$value is not matched any."))
    }

    //def as[B](implicit f: T => B): Matcher[B] = 
    //  Or[B](matchers.map(m => m.as[B]):_*)
  }

  case class PrefixMatcher(prefix: String) extends Matcher[String] {
    lazy val pattern = s"^${prefix}(.*)$$".r
  
    def matches(value: String) = value match {
      case pattern(remains) => Matched(prefix :: HNil)
      case _ => Unmatched(s"$value is not start with $prefix")
    }

    def as[B](implicit f: String => B): Matcher[B] = 
      throw new Exception("PrefixMatcher only support String")
  }

  /**
   * SetContains 
   */
  case class SetContains[T](value: T) extends SetMatcher[T] {
    def matches(values: Set[T]) = values.contains(value) match {
      case true  => Matched(value :: HNil)
      case false => Unmatched(s"$value is not contained in set $values")
    }

    def as[B](implicit f: T => B) = 
      SetContains(f(value))
  }

  case class Contains[T](value: T) extends SeqMatcher[T] {
    /**
     * {@inheritDoc}
     */
    def matches(values: Seq[T]) = values.contains(value) match {
      case true  => Matched(value :: HNil)
      case false => Unmatched(s"$value is not contained on seq $values") 
    }

    def as[B](implicit f: T => B) = 
      Contains(f(value))
  }

  case class MapContainsKey[K](key: K) extends MapMatcher[K, Any] {
    /**
     * {@inheritDoc}
     */
    def matches(values: Map[K, Any]) = values.contains(key) match {
      case true  => Matched((key, values(key)) :: HNil)
      case false => Unmatched(s"$key is not contained on Map $values") 
    }

    def as[B](implicit f: K => B) = 
      MapContainsKey(f(key))
  }
}
