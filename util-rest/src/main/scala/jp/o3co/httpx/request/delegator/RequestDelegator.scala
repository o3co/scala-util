package jp.o3co.httpx.request.delegator

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import spray.routing.Route
import spray.routing.directives.FutureDirectives._
import spray.routing.directives.OnCompleteFutureMagnet
import scala.util.{Success, Failure}

trait RequestDelegator {

  implicit def executionContext: ExecutionContext

  def apply[T](content: T)(handler: PartialFunction[T, Route]): Route = handler(content)

  def apply[T](content: Future[T])(handler: PartialFunction[T, Route]): Route = {
    onComplete(OnCompleteFutureMagnet(content)) {
      // Get separate Future[_] from Any, so can handle with polyphenism(param future overload) 
      //case Success(ret: Future[_]) => apply(ret)(handler)
      case Success(ret) => apply(ret)(handler)
      case Failure(ex)  => throw ex
    }
  }
}
