package o3co.domain

trait DomainName extends DomainToken.ImplicitConversions {

  def name: String
  
  def tokens: Seq[DomainToken]

  def reverse = DomainName(tokens.reverse.map(_.toString): _*)

  override def toString: String = name

  def sub(names: String *) = DomainName((name +: names):_*)
}

case class FQDN(name: String) extends DomainName.NameBased {
  if(!DomainName.validatePattern(name)) {
    throw new IllegalArgumentException(s"Invalid domain name '$name'.")
  }
}

/**
 */
object DomainName {

  def apply(names: String *): DomainName = 
    FQDN(names.mkString("."))

  def toTokens(name: String): Seq[DomainToken] = 
    name.split('.').map(DomainToken(_))

  def validatePattern(name: String): Boolean = 
    name.split('.').find(!DomainToken.validatePattern(_)).isEmpty

  trait NameBased extends DomainName {

    def tokens: Seq[DomainToken] = DomainName.toTokens(name)
  }

  trait TokenBased extends DomainName {
  
    def name: String = tokens.mkString(".")
  }
}
