package jp.o3co.aws
package cloudsearch

import com.amazonaws.services.cloudsearchdomain.{model => awsmodel}
import scala.collection.JavaConversions._

trait ModelConversions extends RequestModelConversions with ResultModelConversions 

object ModelConversions extends ModelConversions

trait BaseModelConversions extends jp.o3co.aws.ImplicitConversions {
  import scala.language.implicitConversions

  implicit def bucketAsScala(model: awsmodel.Bucket): Bucket = {
    Bucket(model.getValue, model.getCount)
  }

  implicit def bucketInfoAsScala(model: awsmodel.BucketInfo): BucketInfo = model

  //implicit def contentTypeAsScala(model: awsmodel.ContentType): ContentType = 

  //implicit def AsScala(model: awsmodel.): = 
  //implicit def AsScala(model: awsmodel.): = 
  //implicit def AsScala(model: awsmodel.): = 
  //implicit def AsScala(model: awsmodel.): = 

  implicit def hitAsScala(model: awsmodel.Hit): Hit =
    Hit(
      model.getId, 
      model.getFields.toMap.map(kv => (kv._1: String, kv._2.toSeq.map(v => v: String))),
      model.getExprs.toMap.map(kv => (kv._1: String, kv._2: String)),
      model.getHighlights.toMap
    )

  implicit def hitsAsScala(model: awsmodel.Hits): Hits = {
    Hits(model.getHit.toSeq.map(v => v: Hit), model.getFound, model.getStart, model.getCursor)
  }

  implicit def resourceIDToStrong(rid: ResourceID): String = rid.toString 

  implicit def stringToResourceID(rid: String): ResourceID = ResourceID(rid)

  implicit def suggesterToString(suggester: Suggester): String = suggester.name

  implicit def stringToSuggester(name: String): Suggester = Suggester(name)

  implicit def queryParserAsScala(parser: awsmodel.QueryParser) = query.QueryParser(parser)

  implicit def queryParserAsAws(parser: query.QueryParser) = parser match {
    case query.QueryParsers.Simple     => awsmodel.QueryParser.Simple
    case query.QueryParsers.Lucene     => awsmodel.QueryParser.Lucene
    case query.QueryParsers.Structured => awsmodel.QueryParser.Structured
    case query.QueryParsers.Dismax     => awsmodel.QueryParser.Dismax
  }

  implicit def queryOptionsToString(options: query.QueryOptions): String = options.toString

}

trait RequestModelConversions extends BaseModelConversions {
  import scala.language.implicitConversions
  import ImplicitConversions._
  
  implicit def queryToSearchRequest(q: query.Query): SearchRequest = SearchRequest(
    query = q
  )

  implicit def searchRequestAsAws(model: SearchRequest): awsmodel.SearchRequest = {
    (new awsmodel.SearchRequest())
      .withQuery(model.query.toString)
      .withQueryParser(model.query.parser)
      .withQueryOptions(model.query.options)
      // Optional value 
      .withCursor(model.cursor.getOrElse(null))
      .withExpr(model.expr.getOrElse(null))
      .withFacet(model.facet.getOrElse(null))
      .withFilterQuery(model.filterQuery.getOrElse(null))
      .withHighlight(model.highlight.getOrElse(null))
      .withPartial(model.partial)
      .withReturn(model.returnValue.getOrElse(null))
      .withSize(model.size.getOrElse(Size(10)): Size)
      .withSort(model.sort.getOrElse(null))
      .withStart(model.start)
  }

  implicit def suggestRequestAsAws(model: SuggestRequest): awsmodel.SuggestRequest = {
    (new awsmodel.SuggestRequest())
      .withQuery(model.query)
      .withSuggester(model.suggester)
      .withSize(model.size)
  }

  implicit def uploadDocumentsRequestAsAws(model: UploadDocumentsRequest): awsmodel.UploadDocumentsRequest = {
    (new awsmodel.UploadDocumentsRequest())
      .withDocuments(model.documents)
      .withContentType(model.contentType)
      .withContentLength(model.contentLength)
  }
}

trait ResultModelConversions extends BaseModelConversions {
  import scala.language.implicitConversions

  implicit def suggestionAsScala(model: awsmodel.SuggestionMatch): Suggestion = 
    Suggestion(model.getSuggestion, model.getScore, model.getId)

  //implicit def suggestionListAsScala(models: java.util.List[awsmodel.SuggestionMatch]): Seq[Suggestion] = 
  //  models.toSeq.map(v => v: Suggestion)

  implicit def searchResultAsScala(model: awsmodel.SearchResult): SearchResult = {
    SearchResult(
      model.getHits, 
      model.getFacets.toMap.map(kv => (kv._1, kv._2: BucketInfo)), 
      model.getStatus.getRid
    ) 
  }

  implicit def suggestResultAsScala(model: awsmodel.SuggestResult): SuggestResult = {
    SuggestResult(
      model.getSuggest.getQuery, 
      model.getSuggest.getFound, 
      model.getSuggest.getSuggestions.toSeq.map(v => v: Suggestion), 
      model.getStatus.getRid
    )
  }

  implicit def uploadDocumentsResultAsScala(model: awsmodel.UploadDocumentsResult): UploadDocumentsResult = {
    UploadDocumentsResult(model.getAdds, model.getDeletes, model.getStatus)
  }
}

