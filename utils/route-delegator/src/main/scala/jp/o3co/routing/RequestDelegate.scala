package jp.o3co.routing

import scala.concurrent.Future
import scala.collection.mutable
import spray.routing.HttpServiceBase
import spray.routing._
import spray.httpx.marshalling.Marshaller
//import jp.o3co.http.StatusMessage
//import spray.http.{StatusCode, StatusCodes}
import spray.routing.directives.OnCompleteFutureMagnet
import scala.concurrent.ExecutionContext
import scala.util.{Success, Failure}

import delegator.RequestDelegator

/***
 * RequestDelegate to mixin delegate method into HttpService
 */
trait RequestDelegate extends Implicits {
  this: HttpServiceBase => 

  //implicit def statusMessageMarshaller: Marshaller[StatusMessage] = implicitly[Marshaller[StatusMessage]]

  implicit def executionContext: ExecutionContext

  //def defaultResponseHandler: Function[Any, Route] = {
  //  case x: String  => complete(StatusMessage(StatusCodes.OK, x))
  //  case x: Int     => complete(StatusMessage(StatusCodes.OK, x))
  //  case x: Double  => complete(StatusMessage(StatusCodes.OK, x))
  //  case x: Float   => complete(StatusMessage(StatusCodes.OK, x))
  //  case x: Long    => complete(StatusMessage(StatusCodes.OK, x))
  //  case x: Boolean => complete(StatusMessage(StatusCodes.OK, x))
  //  case other      => throw new DelegateException(s"Unhandled response: $other")
  //}

  protected lazy val delegators: mutable.Map[DelegateType, RequestDelegator] = mutable.Map() 

  def delegator(delegateType: DelegateType): RequestDelegator = delegators(delegateType)

  def delegate[T](content: => T, typed: DelegateType)(f: T=> Route): Route = 
    delegator(typed).delegate(content)(f)

  def delegateAsync[T](content: => Future[T], typed: DelegateType)(f: T => Route): Route = 
    delegator(typed).delegateAsync(content)(f)
}

