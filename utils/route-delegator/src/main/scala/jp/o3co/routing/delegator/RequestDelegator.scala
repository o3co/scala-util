package jp.o3co.routing.delegator

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import spray.routing.Route
import spray.routing.RequestContext
import spray.routing.directives.FutureDirectives._
import spray.routing.directives.OnCompleteFutureMagnet
import scala.util.{Success, Failure}

trait RequestDelegator {

  implicit def executionContext: ExecutionContext
  /**
   * @param content content to dispatch
   * @param handler Response handler for the dispatch 
   */
  def delegate[T](content: => T)(handler: T => Route): Route = { ctx =>
    onRequest(ctx)

    doHandleResponse(ctx, content, handler)

    onResponse(ctx)
  }

  def delegateAsync[T](content: => Future[T])(handler: T => Route): Route = { ctx =>
    onRequest(ctx)

    onComplete(OnCompleteFutureMagnet(content)) {
      case Success(ret) => 
        (ctx => doHandleResponse(ctx, ret, handler))
      case Failure(e)   => throw e
      case other => 
        throw new Exception("what is it")
    }(ctx)

    onResponse(ctx)
  }

  protected def onRequest(context: RequestContext): Unit = {}

  protected def doHandleResponse[T](context: RequestContext, content: T, handler: T => Route): Unit

  protected def onResponse(context: RequestContext): Unit = {}
}
