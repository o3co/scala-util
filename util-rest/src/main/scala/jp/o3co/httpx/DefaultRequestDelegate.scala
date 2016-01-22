package jp.o3co.httpx

import spray.routing.Route
import scala.concurrent.Future
import spray.routing.HttpService

import request.delegator.RequestDelegator

trait DefaultRequestDelegate extends RequestDelegate {
  this: HttpService => 

  lazy val defaultDelegateType = DelegateType("default")

  def defaultRequestDelegator: RequestDelegator = delegators(defaultDelegateType)

  def defaultRequestDelegator_=(delegator: RequestDelegator) = setDefaultDelegator(delegator)

  def setDefaultDelegator(delegator: RequestDelegator): Unit = {
    delegators.put(defaultDelegateType, delegator)
  }

  def delegate(content: Any)(f: PartialFunction[Any, Route]): Route = {
    delegators(defaultDelegateType)(content)(f)
  }

  def delegate(content: Future[_])(f: PartialFunction[Any, Route]): Route = {
    delegators(defaultDelegateType)(content)(f)
  }
}
