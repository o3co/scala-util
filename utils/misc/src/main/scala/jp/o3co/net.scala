package jp.o3co.net

import java.net.URI
import java.net.URL

/**
 *
 */
case class DomainName(name: String, isFullyQualified: Boolean) {
  // Validate the name
  if(!name.matches(DomainName.LessRestriction)) {
    throw new IllegalArgumentException(s"$name is invalid domain name.")
  }

  /**
   *
   */
  def subdomain(sub: DomainName): DomainName = DomainName(name, sub.name)(isFullyQualified)

  /**
   * Validate this DomainName is a subdomain of given DomainName 
   * {{{
   *  true == DomainName("com.sample").isSubdomainOf(DomainName("com"))
   * }}}
   */
  def isSubdomainOf(root: DomainName): Boolean = name startsWith root.name

  /**
   * Convert this DomainName to sub domainName with given root
   */
  def relativize(root: DomainName) = 
    if(isSubdomainOf(root)) {
      DomainName(name.substring(root.name.size))
    } else {
      throw new IllegalArgumentException(s"Domain $this is not a subdomain of ${root}.")
    }

  override def toString = name
}

/**
 * Companion object of DomainName to provide apply, and Validation pattern
 */
object DomainName {
  
  /**
   * {{{
   *  DomainName("com", "sample") // => DomainName("com.sample")
   * }}}
   */
  def apply(names: String*)(implicit isFullyQualified: Boolean = false): DomainName = 
    new DomainName(names.mkString("."), isFullyQualified)

  /**
   *
   */
  def unappy(domain: DomainName): Option[(String, Boolean)] = Option((domain.name, domain.isFullyQualified))

  /**
   *
   */
  //[a-zA-Z_$][a-zA-Z\d_$]*\.)*[a-zA-Z_$][a-zA-Z\d_$]*
  val LessRestriction = """([a-z0-9][a-z0-9_\-]*\.)*[a-z0-9][a-z0-9_\-]*"""
  //^([a-zA-Z0-9]([a-zA-Z0-9\-]{0,61}[a-zA-Z0-9])?\.)+
  //val Pattern = """\b((?=[a-z0-9-]{1,63}\.)[a-z0-9]+(-[a-z0-9]+)*\.)+[a-z]{2,63}\b"""
  //
  val JavaRestriction = """([\p{L}_$][\p{L}\p{N}_$]*\.)*[\p{L}_$][\p{L}\p{N}_$]*"""
}

/**
 *
 */
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

/**
 * ImplicitConversions from String to Class object
 */
trait ImplicitConversions {
  import scala.language.implicitConversions

  implicit def stringToDomainName(name: String): DomainName = DomainName(name)(false)

  implicit def stringToSlug(name: String): Slug = Slug(name)

  implicit def stringToMailAddress(address: String): MailAddress = MailAddress(address)
}
object ImplicitConversions extends ImplicitConversions

object URL {
  def parseQuery(query: String): Map[String ,String] = {
    if(Option(query).getOrElse("").isEmpty) {
      Map()
    } else {
      (query split '&').map { kv => 
        (kv split '=') match {
          case Array(key, value) => Some(key, value)
          case _ => None
        }
      }
      .collect {
        case Some((k, v)) => (k, v)
      }
      .toMap
    }
  }

  def unapply(url: URL): Option[(String, String, String, Map[String, String])] = Some((
    url.getProtocol(),
    url.getHost(),
    url.getPath(),
    parseQuery(url.getQuery())
  ))

  /**
   * convert 
   * {{{
   *  uri match {
   *    case URL(protocol, host, path, queries) =>
   *  }
   *
   * }}}
   */
  def unapply(uri: URI): Option[(String, String, String, Map[String, String])] = 
    unapply(uri.toURL)
}
