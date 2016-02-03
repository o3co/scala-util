package jp.o3co.httpx.request.delegator

//import jp.o3co.http.StatusMessage
import scala.concurrent.{ExecutionContext, Future}
//import spray.http.{StatusCode, StatusCodes}
//import spray.routing._
//import spray.routing.directives.RouteDirectives._
import spray.routing.Route

/**
 * SimpleRequestDelegator just return the content 
 *
 */
trait SimpleRequestDelegator extends RequestDelegator {

  override def apply[T](content: T)(handler: T => Route): Route = handler(content)
}

object SimpleRequestDelegator {

  def apply() = new SimpleRequestDelegator {}
}

