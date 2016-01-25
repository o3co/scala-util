package jp.o3co.httpx

import scala.concurrent.Future
import spray.routing.Route
import spray.routing.HttpService

import request.delegator.RequestDelegator

trait ReadWriteRequestDelegate extends RequestDelegate {
  this: HttpService => 

  lazy val readRequestDelegateType = DelegateType("read")

  lazy val writeRequestDelegateType = DelegateType("write")

  def readRequestDelegator: RequestDelegator = delegators(readRequestDelegateType)

  def readRequestDelegator_=(delegator: RequestDelegator) =
    setReadRequestDelegator(delegator)

  def writeRequestDelegator: RequestDelegator = delegators(writeRequestDelegateType)

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


  def delegateRead[T](content: T)(f: PartialFunction[T, Route]): Route = {
    readRequestDelegator(content)(f)
  }

  def delegateRead[T](content: Future[T])(f: PartialFunction[T, Route]): Route = {
    readRequestDelegator(content)(f)
  }

  def delegateWrite[T](content: T)(f: PartialFunction[T, Route]): Route = {
    writeRequestDelegator(content)(f)
  }

  def delegateWrite[T](content: Future[T])(f: PartialFunction[T, Route]): Route = {
    writeRequestDelegator(content)(f)
  }
}

