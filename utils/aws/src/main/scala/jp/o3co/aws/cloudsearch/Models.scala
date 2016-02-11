package jp.o3co.aws
package cloudsearch

import com.amazonaws.services.cloudsearchdomain.{model => awsmodel}
import java.io.InputStream
import query.Query
//import query.QueryOptions
//import query.QueryParser


case class ResourceID(rid: String)

  case class Hit(id: String, fields: Map[String, Seq[String]], exprs: Map[String, String], highlights: Map[String, String])

  case class Hits(hit: Seq[Hit], found: Long, start: Long, cursor: String)

  case class Bucket(value: String, count: Long) 

  case class BucketInfo(buckets: Set[Bucket])

  case class Suggestion(suggestion: String, score: Long, id: String)

  case class SearchRequest(
    query: Query, 
    cursor: Option[String] = None, 
    expr:   Option[String] = None, 
    facet:  Option[String] = None, 
    filterQuery: Option[String] = None, 
    highlight: Option[String] = None, 
    partial: Boolean = true,
    //queryOptions: QueryOptions, 
    //queryParser: QueryParser, 
    returnValue: Option[String] = Option("_no_fields"), 
    size: Option[Size] = None, 
    sort: Option[String] = None, 
    start: Long = 0L 
  ) {
    def withReturnAllFields = copy(returnValue = Option("_all_fields"))
    def withoutReturnAny    = copy(returnValue = Option("_no_fields"))
    def withReturnOnlyScore = copy(returnValue = Option("_score"))
    def withReturn(fields: Seq[String]) = copy(returnValue = Option(fields.mkString(",")))
  }
  
  case class SearchResult(hits: Hits, facets: Map[String, BucketInfo], rid: ResourceID)

  case class SuggestRequest(query: String, suggester: Suggester, size: Size = Size(10))

  case class SuggestResult(query: String, found: Long, suggestions: Seq[Suggestion], rid: ResourceID)

  case class UploadDocumentsRequest(documents: InputStream, contentLength: Long, contentType: String)

  case class UploadDocumentsResult(adds: Long, deletes: Long, rid: ResourceID)
