package o3co.net

/**
 * MailAddress object.
 *
 * @param address Mail address
 */
case class MailAddress(address: String) {
  import org.apache.commons.validator.routines.EmailValidator

  if(!EmailValidator.getInstance().isValid(address)) {
    throw new Exception(s"Address $address is invalid email address.")
  }

  override def toString: String = address
}
