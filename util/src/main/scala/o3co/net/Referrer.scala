package o3co.net

/**
 *
 */
trait Referrer {
  /**
   *
   */
  def uri: URI
}

/**
 *
 */
case class SimpleReferrer(uri: URI) extends Referrer

/**
 *
 */
object Referrer {

  def apply(uri: String) = SimpleReferrer(new URI(uri))
  
  /**
   *
   */
  def apply(uri: URI) = SimpleReferrer(uri)

  /**
   *
   */
  def apply(url: URL) = SimpleReferrer(url.toURI)
}
