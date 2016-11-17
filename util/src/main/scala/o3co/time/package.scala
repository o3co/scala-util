package o3co

import java.time._

/**
 */
package object time {

  /**
   *
   */
  implicit class ZonedDateTimeExtension(val zdt: ZonedDateTime) {
    def toUTC: ZonedDateTime = zdt.withZoneSameInstant(ZoneOffset.UTC)
  }

  implicit class LocalDateTimeExtension(val ldt: LocalDateTime) {
    def toUTC: ZonedDateTime = ldt.atZone(ZoneOffset.UTC)
  }
}
