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

  def delegate[T](content: T)(f: PartialFunction[T, Route]): Route = {
    delegators(defaultDelegateType)(content)(f)
  }

  def delegate[T](content: Future[T])(f: PartialFunction[T, Route]): Route = {
    delegators(defaultDelegateType)(content)(f)
  }
}
