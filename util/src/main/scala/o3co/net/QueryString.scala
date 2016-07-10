package o3co.net

case class QueryString(query: String) {
  def toMap: Map[String ,String] = QueryString.parse(query)
}

object QueryString {
  def parse(query: String): Map[String, String] = {
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
}
