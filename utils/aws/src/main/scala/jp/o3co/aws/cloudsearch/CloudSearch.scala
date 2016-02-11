package jp.o3co.aws
package cloudsearch

import java.io.InputStream
import java.io.ByteArrayInputStream

/**
 * Interface for wrapper of AmazonCloudSearchDomainClient
 * Causion: 
 *   Endpoint of uploadDocuments and search/suggest are differ.
 *   To upload and search(or suggest), you have to create two different instance for them. 
 */
trait CloudSearch extends ModelConversions {

  implicit val size: Size = Size(10) 

  /**
   * Upload Documents as Traversable[Document] 
   */
  def uploadDocuments(documents: Documents): UploadDocumentsResult

  /**
   * Upload Documents as String
   *
   */
  def uploadDocuments(documents: String, contentType: String): UploadDocumentsResult = 
    uploadDocuments(documents.getBytes, contentType)

  def uploadDocuments(documents: Array[Byte], contentType: String): UploadDocumentsResult = {
    val is = new ByteArrayInputStream(documents)
    try {
      uploadDocuments(is, documents.size, contentType)
    } finally {
      is.close()
    }
  }

  def uploadDocuments(content: InputStream, contentLength: Int, contenType: String): UploadDocumentsResult = 
    uploadDocuments(UploadDocumentsRequest(content, contentLength, contenType))

  def uploadDocuments(request: UploadDocumentsRequest): UploadDocumentsResult

  def search(request: SearchRequest): SearchResult

  def suggest(query: String)(implicit suggester: Suggester, size: Size): SuggestResult = 
    suggest(SuggestRequest(query, suggester, size))

  def suggest(request: SuggestRequest): SuggestResult 
}

