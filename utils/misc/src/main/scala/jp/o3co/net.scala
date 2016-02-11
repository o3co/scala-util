package jp.o3co.net

class DomainName(val name: String) {
  if(!name.matches(DomainName.Pattern)) {
    throw new IllegalArgumentException(s"$name is invalid domain name.")
  }

  /**
   *
   */
  override def toString: String = name
}

/**
 *
 */
object DomainName {
  /**
   *
   */
  def apply(name: String): DomainName = new DomainName(name)

  def unapply(domain: DomainName): Option[(String)] = Some((domain.name))

  /**
   *
   */
  val Pattern = """\b((?=[a-z0-9-]{1,63}\.)[a-z0-9]+(-[a-z0-9]+)*\.)+[a-z]{2,63}\b"""
}

case class Slug(name: String) {
  if(!name.matches(Slug.Pattern)) {
    throw new IllegalArgumentException(s"$name is invalid slug.")
  }

  override def toString: String = name
}

object Slug {
  val Pattern = "[a-z0-9-_]+"
}

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

trait ImplicitConversions {
  import scala.language.implicitConversions

  implicit def stringToDomainName(name: String): DomainName = DomainName(name)

  implicit def stringToSlug(name: String): Slug = Slug(name)

  implicit def stringToMailAddress(address: String): MailAddress = MailAddress(address)
}
object ImplicitConversions extends ImplicitConversions
