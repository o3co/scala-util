package jp.o3co.routing.delegator

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import spray.routing.{Route, StandardRoute, RequestContext}
import spray.http.StatusCodes
import scala.concurrent.{ExecutionContext, Future}
import spray.routing.HttpService
import spray.testkit.Specs2RouteTest
import spray.httpx.Json4sSupport
import org.json4s.{Formats, DefaultFormats}
import spray.httpx.marshalling.Marshaller

class SimpleRequestDelegatorSpec extends Specification with Specs2RouteTest with HttpService with SimpleRequestDelegator with Json4sSupport {

  def actorRefFactory = system

  implicit val json4sFormats = DefaultFormats

  implicit val executionContext = ExecutionContext.Implicits.global

  def testRoute(r: Route) = sealRoute(get {
    pathEndOrSingleSlash {
      r
    }
  })

  case class Content(value: String) 

  "SimpleRequestDelegator" should {
    "return StandardRoute for handled content" in {
      delegate(Content("test")) { content => 
        complete(content.value)
      } must beAnInstanceOf[Route]
    }
    //"throw an DelegateException when non-primitive is delegated." in {
    //  apply(Content("test"))(PartialFunction.empty) must throwA[DelegateException]
    //}

    "handle on route" in {
      def route(content: Any) = testRoute(delegate(content) { 
        case Content(str) => complete(StatusCodes.OK, str)
        case other   => complete(StatusCodes.BadRequest)
      })
      
      Get() ~> route(Content("hello"))~> check {
        //status = StatusCodes.OK
        status should be equalTo StatusCodes.OK
        entity.asString must be equalTo s""""hello""""
      }

      Get() ~> route("hello")~> check {
        //status = StatusCodes.OK
        status should be equalTo StatusCodes.BadRequest
      }
    }
  }
}

