package o3co

//import java.net.URI
//import java.net.URL

package object net {

  type URL = java.net.URL
  type URI = java.net.URI

  object URL {
    def unapply(url: URL): Option[(String, String, Int, String, Map[String, String], String)] = 
      Some((
        url.getProtocol(),
        url.getHost(),
        url.getPort(),
        url.getPath(),
        QueryString.parse(url.getQuery()),
        url.getRef()
      ))
  }

  object URI {
    /**
     * Unapply to 
     *   - protocol
     *   - host
     *   - port 
     *   - path
     *   - queries
     *   - fragment
     */
    def unapply(uri: URI): Option[(String, String, Int, String, Map[String, String], String)] = 
      Some((
        uri.getScheme(),
        uri.getHost(),
        uri.getPort(),
        uri.getPath(),
        QueryString.parse(uri.getQuery()),
        uri.getFragment()
      ))
  }

}
