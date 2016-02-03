package jp.o3co.httpx

import scala.concurrent.Future
import scala.collection.mutable
import spray.routing.HttpServiceBase
import spray.routing._
import spray.httpx.marshalling.Marshaller
import jp.o3co.http.StatusMessage
import spray.http.{StatusCode, StatusCodes}
import spray.routing.directives.OnCompleteFutureMagnet
import scala.concurrent.ExecutionContext
import scala.util.{Success, Failure}

import request.delegator.RequestDelegator

/***
 * RequestDelegate to mixin delegate method into HttpService
 */
trait RequestDelegate extends Implicits {
  this: HttpServiceBase => 

  //case class Delegatee[T](delegator: RequestDelegator, content: T)(handler: T => Route) {
  //  
  //  def apply(value: T)
  //  def apply(ctx: RequestContext): Unit = delegator( 
  //}

  implicit def statusMessageMarshaller: Marshaller[StatusMessage] = implicitly[Marshaller[StatusMessage]]

  implicit def executionContext: ExecutionContext

  def defaultResponseHandler: Function[Any, Route] = {
    case x: String  => complete(StatusMessage(StatusCodes.OK, x))
    case x: Int     => complete(StatusMessage(StatusCodes.OK, x))
    case x: Double  => complete(StatusMessage(StatusCodes.OK, x))
    case x: Float   => complete(StatusMessage(StatusCodes.OK, x))
    case x: Long    => complete(StatusMessage(StatusCodes.OK, x))
    case x: Boolean => complete(StatusMessage(StatusCodes.OK, x))
    case other      => throw new DelegateException(s"Unhandled response: $other")
  }

  lazy val delegators: mutable.Map[DelegateType, RequestDelegator] = mutable.Map() 

  def delegate[T](content: T, typed: DelegateType)(f: T => Route): Route = {
    delegators(typed)(content)(f)
  }

  def delegate[T](content: Future[T], typed: DelegateType)(f: T => Route): Route = {
    onComplete(OnCompleteFutureMagnet(content)) {
      case Success(ret) => delegators(typed)(ret)(f)
      case Failure(ex)     => throw ex
    }
  }
}

