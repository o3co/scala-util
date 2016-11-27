package o3co.time

import java.time._

package object order {

  /**
   *
   */
  implicit class LocalDateTimeOrdered(ldt: LocalDateTime) extends Ordered[LocalDateTime] {
    
    /**
     *
     */
    def compare(that: LocalDateTime) = 
      ldt.compareTo(that)
  }

  /**
   *
   */
  implicit class LocalDateOrdered(ldt: LocalDate) extends Ordered[LocalDate] {
    
    /**
     *
     */
    def compare(that: LocalDate) = 
      ldt.compareTo(that)
  }

  /**
   *
   */
  implicit class LocalTimeOrdered(ldt: LocalTime) extends Ordered[LocalTime] {
    
    /**
     *
     */
    def compare(that: LocalTime) = 
      ldt.compareTo(that)
  }
}
