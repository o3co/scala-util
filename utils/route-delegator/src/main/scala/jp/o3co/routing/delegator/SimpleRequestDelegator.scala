package jp.o3co.routing.delegator

import scala.concurrent.{ExecutionContext, Future}
import spray.routing.RequestContext
import spray.routing.Route

/**
 * SimpleRequestDelegator just return the content 
 *
 */
trait SimpleRequestDelegator extends RequestDelegator {

  protected def doHandleResponse[T](context: RequestContext, content: T, handler: T => Route): Unit = {
    // Evaluate given Route
    handler(content)(context)
  }
}

object SimpleRequestDelegator {

  def apply()(implicit ec: ExecutionContext) = new SimpleRequestDelegator {
    implicit val executionContext = ec
  }

  def apply(onRequestHandler: RequestContext => Unit, onResponseHandler: RequestContext => Unit)(implicit ec: ExecutionContext) = 
    new SimpleRequestDelegator {
      implicit val executionContext = ec
      override protected def onRequest(context: RequestContext) = onRequestHandler(context)
      override protected def onResponse(context: RequestContext) = onResponseHandler(context)
    }
}

