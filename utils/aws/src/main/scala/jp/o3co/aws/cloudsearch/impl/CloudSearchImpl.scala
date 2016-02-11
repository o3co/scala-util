package jp.o3co.aws
package cloudsearch

import java.io.InputStream
import com.amazonaws.services.cloudsearchdomain

trait CloudSearchImpl extends CloudSearch {

  def credentials: Credentials

  def endpoint: Endpoint

  /**
   * SDK Client
   */
  protected lazy val awsclient = {
    val c = new cloudsearchdomain.AmazonCloudSearchDomainClient(credentials)
    c.setEndpoint(endpoint)
    c
  }

  def uploadDocuments(documents: Documents) = 
    uploadDocuments(documents.toJson, "application/json")

  override def uploadDocuments(request: UploadDocumentsRequest) = 
    awsclient.uploadDocuments(request)

  def search(request: SearchRequest) = 
    awsclient.search(request)

  def suggest(request: SuggestRequest) = 
    awsclient.suggest(request)
}

object CloudSearchImpl {
  def apply(implicit ep: Endpoint, c: Credentials) = new CloudSearchImpl {
    override val endpoint = ep
    override val credentials = c
  }
}
