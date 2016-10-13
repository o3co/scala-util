package o3co.net

/**
 *
 */
case class Domain(name: String, isFullyQualified: Boolean) {
  // Validate the name
  if(!name.matches(Domain.LessRestriction)) {
    throw new IllegalArgumentException(s"$name is invalid domain name.")
  }

  /**
   * {{{
   *   Domain("sample.com").reverse 
   *   // Domain("com.sample")
   * }}}
   */
  def reverse: Domain = 
    Domain(name.split('.').reverse.mkString("."))

  /**
   *
   */
  def subdomain(sub: Domain): Domain = Domain(name, sub.name)(isFullyQualified)

  /**
   * Validate this Domain is a subdomain of given Domain 
   * {{{
   *  true == Domain("com.sample").isSubdomainOf(Domain("com"))
   * }}}
   */
  def isSubdomainOf(root: Domain): Boolean = name startsWith root.name

  /**
   * Convert this Domain to sub domainName with given root
   */
  def relativize(root: Domain) = 
    if(isSubdomainOf(root)) {
      Domain(name.substring(root.name.size))
    } else {
      throw new IllegalArgumentException(s"Domain $this is not a subdomain of ${root}.")
    }

  override def toString = name
}

/**
 * Companion object of Domain to provide apply, and Validation pattern
 */
object Domain {
  
  /**
   * {{{
   *  Domain("com", "sample") // => Domain("com.sample")
   * }}}
   *
   * @param names Seq of names
   * @param isFullyQualified True if this domain is fully qualified. False otherwise. 
   */
  def apply(names: String*)(implicit isFullyQualified: Boolean = false): Domain = 
    new Domain(names.mkString("."), isFullyQualified)

  def fqdn(names: String *): Domain = new Domain(names.mkString("."), true)

  /**
   *
   */
  def unappy(domain: Domain): Option[(String, Boolean)] = Option((domain.name, domain.isFullyQualified))

  /**
   *
   */
  val LessRestriction = """([a-z0-9][a-z0-9_\-]*\.)*[a-z0-9][a-z0-9_\-]*"""

  val JavaRestriction = """([\p{L}_$][\p{L}\p{N}_$]*\.)*[\p{L}_$][\p{L}\p{N}_$]*"""
}
