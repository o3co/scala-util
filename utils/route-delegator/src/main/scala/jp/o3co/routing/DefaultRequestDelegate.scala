package jp.o3co.routing

import spray.routing.Route
import scala.concurrent.Future
import spray.routing.HttpService

import delegator.RequestDelegator

trait DefaultRequestDelegate extends RequestDelegate {
  this: HttpService => 

  lazy val defaultDelegateType = DelegateType("default")

  def defaultRequestDelegator: RequestDelegator = delegators(defaultDelegateType)

  def defaultRequestDelegator_=(delegator: RequestDelegator) = setDefaultRequestDelegator(delegator)

  def setDefaultRequestDelegator(delegator: RequestDelegator): Unit = {
    delegators.put(defaultDelegateType, delegator)
  }

  override def delegator(delegateType: DelegateType): RequestDelegator = delegators.getOrElse(delegateType, defaultRequestDelegator)

  def delegate[T](content: => T)(f: T => Route): Route = 
    delegate(content, defaultDelegateType)(f)

  def delegateAsync[T](content: => Future[T])(f: T => Route): Route = {
    delegateAsync(content, defaultDelegateType)(f)
  }
}
