package jp.o3co.routing

import scala.concurrent.Future
import spray.routing.Route
import spray.routing.HttpService

import delegator.RequestDelegator

trait ReadWriteRequestDelegate extends RequestDelegate {
  this: HttpService => 

  lazy val readRequestDelegateType = DelegateType("read")

  lazy val writeRequestDelegateType = DelegateType("write")

  def readRequestDelegator: RequestDelegator = delegator(readRequestDelegateType)

  def readRequestDelegator_=(delegator: RequestDelegator) =
    setReadRequestDelegator(delegator)

  def writeRequestDelegator: RequestDelegator = delegator(writeRequestDelegateType)

  def writeRequestDelegator_=(delegator: RequestDelegator) =
    setWriteRequestDelegator(delegator)

  def readWriteRequestDelegator_=(delegator: RequestDelegator) = 
    setReadWriteRequestDelegator(delegator)

  def setReadRequestDelegator(delegator: RequestDelegator): Unit = {
    delegators.put(readRequestDelegateType, delegator)
  }

  def setWriteRequestDelegator(delegator: RequestDelegator): Unit = {
    delegators.put(writeRequestDelegateType, delegator)
  }

  def setReadWriteRequestDelegator(delegator: RequestDelegator): Unit = {
    delegators.put(readRequestDelegateType, delegator)
    delegators.put(writeRequestDelegateType, delegator)
  }


  def delegateRead[T](content: => T)(f: T => Route): Route = 
    delegate(content, readRequestDelegateType)(f)

  def delegateReadAsync[T](content: => Future[T])(f: T => Route): Route = 
    delegateAsync(content, readRequestDelegateType)(f)

  def delegateWrite[T](content: => T)(f: T => Route): Route = 
    delegate(content, writeRequestDelegateType)(f)

  def delegateWriteAsync[T](content: => Future[T])(f: T => Route): Route =
    delegateAsync(content, writeRequestDelegateType)(f)
}

