package jp.o3co.httpx

import scala.concurrent.Future
import scala.collection.mutable
import spray.routing.HttpServiceBase
import spray.routing.Route

import request.delegator.RequestDelegator

trait Implicits extends DelegateType.Implicits

trait RequestDelegate extends Implicits {
  this: HttpServiceBase => 

  lazy val delegators: mutable.Map[DelegateType, RequestDelegator] = mutable.Map() 

  def delegate(content: Any, typed: DelegateType)(f: PartialFunction[Any, Route]): Route = {
    delegators(typed)(content)(f)
  }

  def delegate(content: Future[_], typed: DelegateType)(f: PartialFunction[Any, Route]): Route = {
    delegators(typed)(content)(f)
  }
}

