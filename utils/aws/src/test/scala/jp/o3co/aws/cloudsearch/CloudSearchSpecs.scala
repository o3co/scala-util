package jp.o3co.aws
package cloudsearch

import org.specs2.mutable.Specification
import jp.o3co.config._
import jp.o3co.aws.config._

/**
 *
 *  Cloudsearch test works with CloudSearch Predefined data (IMDB movies demo)
 */
class CloudSearchSpecs extends Specification {

  import ModelConversions._

  args(skipAll = !AWSTestSettings.isRemoteTestEnabled)

  "CloudSearch" should {
    "search" in {

      val settings    = AWSEndpointSettings(GlobalSettings.getConfig("aws.test.cloudsearch"))
      val credentials = settings.credentials
      val endpoint    = settings.endpointFor("cloudsearch")

      val client      = CloudSearchImpl(endpoint.forSearch, credentials)

      val res = client.search(query.SimpleQuery("World"))

      res.hits.found === 310
      res.hits.start === 0
      res.hits.hit.size === 10
    }
    "search" in {
      val settings    = AWSEndpointSettings(GlobalSettings.getConfig("aws.test.cloudsearch"))
      val credentials = settings.credentials
      val endpoint    = settings.endpointFor(AWSServices.CloudSearch)

      val client = CloudSearchImpl(endpoint.forSearch, credentials)

      val res = client.search(query.SimpleQuery("World").toRequest.withReturnAllFields)

      println(res)
      res.hits.found === 310
      res.hits.start === 0
      res.hits.hit.size === 10
    }
    "suggests" in {

      val settings    = AWSEndpointSettings(GlobalSettings.getConfig("aws.test.cloudsearch"))
      val credentials = settings.credentials
      val endpoint    = settings.endpointFor(AWSServices.CloudSearch)

      val client = CloudSearchImpl(endpoint.forSearch, credentials)

      implicit val suggester = Suggester("title")
      implicit val size: Size = Size(10)

      val res = client.suggest("Hist")

      res.found === 2
    }
    "uploadDocuments" in {
      val settings    = AWSEndpointSettings(GlobalSettings.getConfig("aws.test.cloudsearch"))
      val credentials = settings.credentials
      val endpoint    = settings.endpointFor(AWSServices.CloudSearch)


      val client = CloudSearchImpl(endpoint.forDocuments, credentials)

      import FieldValueConversions._
      val documents = Documents()
      documents
        .addDocument("foo", FieldValues(
          "actors" -> Seq("Taro", "Jiro"),
          "directors" -> Seq("Sato", "Saito"),
          "genres" -> Seq("dummy")
        ))
      val res = client.uploadDocuments(documents)

      println(res)
      true
    }
  }
}

