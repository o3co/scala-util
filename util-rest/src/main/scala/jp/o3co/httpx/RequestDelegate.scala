package jp.o3co.httpx

import scala.concurrent.Future
import scala.collection.mutable
import spray.routing.HttpServiceBase
import spray.routing.Route

import request.delegator.RequestDelegator

/***
 * RequestDelegate to mixin delegate method into HttpService
 */
trait RequestDelegate extends Implicits {
  this: HttpServiceBase => 

  lazy val delegators: mutable.Map[DelegateType, RequestDelegator] = mutable.Map() 

  def delegate[T](content: T, typed: DelegateType)(f: PartialFunction[T, Route]): Route = {
    delegators(typed)(content)(f)
  }

  def delegate[T](content: Future[T], typed: DelegateType)(f: PartialFunction[T, Route]): Route = {
    delegators(typed)(content)(f)
  }
}

