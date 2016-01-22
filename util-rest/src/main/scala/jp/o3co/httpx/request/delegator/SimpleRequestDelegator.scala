package jp.o3co.httpx.request.delegator

import jp.o3co.http.StatusMessage
import scala.concurrent.{ExecutionContext, Future}
import spray.http.{StatusCode, StatusCodes}
import spray.httpx.marshalling.Marshaller
import spray.routing._
import spray.routing.directives.RouteDirectives._
import spray.routing.Route

/**
 * SimpleRequestDelegator just return the content 
 *
 */
trait SimpleRequestDelegator extends RequestDelegator {
  implicit def statusMessageMarshaller: Marshaller[StatusMessage]

  val defaultResponseHandler: PartialFunction[Any, Route] = {
    case x: String  => complete(StatusMessage(StatusCodes.OK, x))
    case x: Int     => complete(StatusMessage(StatusCodes.OK, x))
    case x: Double  => complete(StatusMessage(StatusCodes.OK, x))
    case x: Float   => complete(StatusMessage(StatusCodes.OK, x))
    case x: Long    => complete(StatusMessage(StatusCodes.OK, x))
    case x: Boolean => complete(StatusMessage(StatusCodes.OK, x))
    case other      => throw new DelegateException(s"Unhandled response: $other")
  }

  override def apply(content: Any)(handler: PartialFunction[Any, Route]): Route = 
    (handler orElse defaultResponseHandler)(content)
}

object SimpleRequestDelegator {
  def apply()(implicit smm: Marshaller[StatusMessage], ec: ExecutionContext) = new SimpleRequestDelegator {
    override val statusMessageMarshaller = smm
    override val executionContext = ec
  }
}
