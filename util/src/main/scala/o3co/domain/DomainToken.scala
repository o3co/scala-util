package o3co.domain

/**
 */
trait DomainToken {
  def token: String 

  override def toString: String = token
}

case class ConcreteDomainToken(token: String) extends DomainToken{
  /**
   */
  if(!DomainToken.validatePattern(token)) {
    throw new IllegalArgumentException(s"Invalid token pattern '$token'")
  }
}

/**
 */
object DomainToken {
  val Pattern = """[a-z0-9][a-z0-9_\-]*"""

  def validatePattern(value: String): Boolean = 
    value.matches(Pattern)

  def apply(token: String): DomainToken = 
    ConcreteDomainToken(token)

  trait ImplicitConversions {
    implicit def stringToToken(token: String): DomainToken = 
      ConcreteDomainToken(token)
  }

  def unapply(token: DomainToken): Option[String] = 
    Some(token.token)
}

