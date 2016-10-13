package o3co.net

trait UserAgent {
  def name: String
}

case class SimpleUserAgent(name: String) extends UserAgent

object UserAgent {
  
  def apply(name: String): UserAgent = SimpleUserAgent(name)
}
