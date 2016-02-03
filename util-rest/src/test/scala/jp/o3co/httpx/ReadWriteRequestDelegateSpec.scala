package jp.o3co.httpx

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import spray.routing.{Route, StandardRoute, RequestContext}
import spray.http.StatusCodes
import scala.concurrent.{ExecutionContext, Future}
import jp.o3co.httpx.request.delegator.SimpleRequestDelegator
import spray.routing.HttpService
import spray.testkit.Specs2RouteTest

class ReadWriteRequestDelegateSpec extends Specification with Specs2RouteTest with HttpService with ReadWriteRequestDelegate {

  def actorRefFactory = system

  val _default = new SimpleRequestDelegator {}

  implicit val executionContext = ExecutionContext.Implicits.global
  setReadWriteRequestDelegator(_default)

  def testRoute(r: Route) = sealRoute(get {
    pathEndOrSingleSlash {
      r
    }
  })

  sequential

  "ReadWriteDelegate" should {
    "set/get readRequestDelegator" in {
      val tmp  = new SimpleRequestDelegator {}

      readRequestDelegator !== tmp

      readRequestDelegator = tmp
      readRequestDelegator === tmp

      readRequestDelegator = _default 
      true
    }
    "set/get writeRequestDelegator" in {
      val tmp  = new SimpleRequestDelegator {}
      readRequestDelegator !== tmp

      readRequestDelegator = tmp
      readRequestDelegator === tmp

      readRequestDelegator = _default 
      true
    }
    "handle content with decorated route" in {
      Get() ~> testRoute(delegateRead("hello") {
        case "hello" => complete(StatusCodes.OK)
        case other   => complete(StatusCodes.BadRequest)
      }) ~> check {
        //status = StatusCodes.OK
        status should be equalTo StatusCodes.OK
      }

      Get() ~> testRoute(delegateRead("not hello") {
        case "hello" => complete(StatusCodes.OK)
        case other   => complete(StatusCodes.BadRequest)
      }) ~> check {
        //status = StatusCodes.OK
        status should be equalTo StatusCodes.BadRequest
      }

      // Write
      Get() ~> testRoute(delegateWrite("hello") {
        case "hello" => complete(StatusCodes.OK)
        case other   => complete(StatusCodes.BadRequest)
      }) ~> check {
        //status = StatusCodes.OK
        status should be equalTo StatusCodes.OK
      }

      Get() ~> testRoute(delegateWrite("not hello") {
        case "hello" => complete(StatusCodes.OK)
        case other   => complete(StatusCodes.BadRequest)
      }) ~> check {
        //status = StatusCodes.OK
        status should be equalTo StatusCodes.BadRequest
      }
    }
    "handle future content with decorated route " in {
      Get() ~> testRoute(delegateRead(Future("hello")) {
        case "hello" => complete(StatusCodes.OK)
        case other   => complete(StatusCodes.BadRequest)
      }) ~> check {
        //status = StatusCodes.OK
        status should be equalTo StatusCodes.OK
      }

      Get() ~> testRoute(delegateRead(Future("not hello")) {
        case "hello" => complete(StatusCodes.OK)
        case other   => complete(StatusCodes.BadRequest)
      }) ~> check {
        //status = StatusCodes.OK
        status should be equalTo StatusCodes.BadRequest
      }

      //// Recursive futures
      //Get() ~> testRoute(delegateRead(Future(Future(Future("hello")))) {
      //  case "hello" => complete(StatusCodes.OK)
      //  case other   => complete(StatusCodes.BadRequest)
      //}) ~> check {
      //  //status = StatusCodes.OK
      //  status should be equalTo StatusCodes.OK
      //}

      Get() ~> testRoute(delegateWrite(Future("hello")) {
        case "hello" => complete(StatusCodes.OK)
        case other   => complete(StatusCodes.BadRequest)
      }) ~> check {
        //status = StatusCodes.OK
        status should be equalTo StatusCodes.OK
      }

      Get() ~> testRoute(delegateWrite(Future("not hello")) {
        case "hello" => complete(StatusCodes.OK)
        case other   => complete(StatusCodes.BadRequest)
      }) ~> check {
        //status = StatusCodes.OK
        status should be equalTo StatusCodes.BadRequest
      }

      //// Recursive futures
      //Get() ~> testRoute(delegateWrite(Future(Future(Future("hello")))) {
      //  case "hello" => complete(StatusCodes.OK)
      //  case other   => complete(StatusCodes.BadRequest)
      //}) ~> check {
      //  //status = StatusCodes.OK
      //  status should be equalTo StatusCodes.OK
      //}
    }
  }
}

