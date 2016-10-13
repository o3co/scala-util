package o3co.duration

import java.time.LocalDateTime

trait ImplicitExtensions {
  /**
   */
  implicit class LocalDateTimeExtension(val ldt: LocalDateTime) {

    def greaterThan(other: LocalDateTime): Boolean = 
      ldt.compareTo(other) > 0

    def greaterThanOrEquals(other: LocalDateTime): Boolean = 
      ldt.compareTo(other) >= 0

    def lessThan(other: LocalDateTime): Boolean = 
      ldt.compareTo(other) < 0

    def lessThanOrEquals(other: LocalDateTime): Boolean = 
      ldt.compareTo(other) <= 0

    def >(other: LocalDateTime): Boolean = 
      greaterThan(other)

    def >=(other: LocalDateTime): Boolean = 
      greaterThanOrEquals(other)

    def <(other: LocalDateTime): Boolean = 
      lessThan(other)

    def <=(other: LocalDateTime): Boolean = 
      lessThanOrEquals(other)
  }
}

trait Implicits extends ImplicitExtensions
