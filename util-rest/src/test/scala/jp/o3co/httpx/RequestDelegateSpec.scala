package jp.o3co.httpx

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import spray.routing.{Route, StandardRoute, RequestContext}
import spray.http.StatusCodes
import scala.concurrent.{ExecutionContext, Future}
import jp.o3co.httpx.request.delegator.RequestDelegator
import spray.routing.HttpService
import spray.testkit.Specs2RouteTest

class RequestDelegateSpec extends Specification with Specs2RouteTest with HttpService with RequestDelegate {

  def actorRefFactory = system


  delegators.put("test", new RequestDelegator() {
    override val executionContext = ExecutionContext.Implicits.global 
  })

  def testRoute(r: Route) = sealRoute(get {
    pathEndOrSingleSlash {
      r
    }
  })

  "RequestDelegate" should {
    "return StandardRoute" in {
      val route = delegate("hello", "test") {
        case str: String => complete(StatusCodes.OK)
        case _ => complete(StatusCodes.BadRequest)
      }
      route must beAnInstanceOf[StandardRoute]
    }
    "handle content with decorated route" in {
      Get() ~> testRoute(delegate("hello", "test") {
        case "hello" => complete(StatusCodes.OK)
        case other   => complete(StatusCodes.BadRequest)
      }) ~> check {
        //status = StatusCodes.OK
        status should be equalTo StatusCodes.OK
      }

      Get() ~> testRoute(delegate("not hello", "test") {
        case "hello" => complete(StatusCodes.OK)
        case other   => complete(StatusCodes.BadRequest)
      }) ~> check {
        //status = StatusCodes.OK
        status should be equalTo StatusCodes.BadRequest
      }
    }
    "handle future content with decorated route " in {
      Get() ~> testRoute(delegate(Future("hello"), "test") {
        case "hello" => complete(StatusCodes.OK)
        case other   => complete(StatusCodes.BadRequest)
      }) ~> check {
        //status = StatusCodes.OK
        status should be equalTo StatusCodes.OK
      }

      Get() ~> testRoute(delegate(Future("not hello"), "test") {
        case "hello" => complete(StatusCodes.OK)
        case other   => complete(StatusCodes.BadRequest)
      }) ~> check {
        //status = StatusCodes.OK
        status should be equalTo StatusCodes.BadRequest
      }

      // Recursive futures
      Get() ~> testRoute(delegate(Future(Future(Future("hello"))), "test") {
        case "hello" => complete(StatusCodes.OK)
        case other   => complete(StatusCodes.BadRequest)
      }) ~> check {
        //status = StatusCodes.OK
        status should be equalTo StatusCodes.OK
      }
    }
  }
}
