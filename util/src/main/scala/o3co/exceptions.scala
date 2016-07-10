package o3co.exceptions

trait NotFound

trait Duplicated

trait Restriction 

case class NotFoundException(message: String = null, cause: Throwable = null) extends RuntimeException(message, cause) with NotFound

case class DuplicatedException(message: String = null, cause: Throwable = null) extends RuntimeException(message, cause) with Duplicated

case class RestrictionException(message: String = null, cause: Throwable = null) extends RuntimeException(message, cause) with Restriction
