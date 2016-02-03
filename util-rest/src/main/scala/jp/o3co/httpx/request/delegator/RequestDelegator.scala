package jp.o3co.httpx.request.delegator

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import spray.routing.Route
import spray.routing.directives.FutureDirectives._
import spray.routing.directives.OnCompleteFutureMagnet
import scala.util.{Success, Failure}

trait RequestDelegator {
  /**
   * @param content content to dispatch
   * @param handler Response handler for the dispatch 
   */
  def apply[T](content: T)(handler: T => Route): Route
}
