package jp.o3co.generator

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

/**
 * Base trait of AsyncGenerator
 */
trait AsyncGenerator[T] {

  /**
   *
   */
  def generateAsync(): Future[T]
}

/**
 * AsAsyncGenerator mixin the behavior of AsyncGenerator into Generator 
 */
trait AsAsyncGenerator[T] extends AsyncGenerator[T] {
  this: Generator[T] =>

  implicit def executionContext: ExecutionContext

  /**
   *
   */
  def generateAsync() = Future(generate())
}

