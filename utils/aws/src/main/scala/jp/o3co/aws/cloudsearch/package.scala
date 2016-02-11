package jp.o3co.aws

package object cloudsearch {
  
  /**
   * Base trait of Implicit class for Endpoint 
   */
  trait CloudSearchEndpointLike {
    def endpoint: Endpoint

    /**
     * Create PathEndpoint for Search
     */
    def forSearch: Endpoint = PathEndpoint(s"search-${endpoint.path}")

    /**
     * Create PathEndpoint for Documents 
     */
    def forDocuments: Endpoint = PathEndpoint(s"doc-${endpoint.path}")
  }

  /**
   * Implicit class as appending Extensional function to endpoint.
   * There are two Endpoints for CloudSearch. One is for searching and another is for documents.
   * With CloudSearchEndpintLike, Endpoint can be the general endpoint, and can provide both for search and documents
   */
  implicit class CloudSearchEndpoint(val endpoint: Endpoint) extends CloudSearchEndpointLike

  /**
   * Case class for Suggester name.
   * To provide implicit suggester easier.
   */
  case class Suggester(name: String)
}
