package o3co.generate

import o3co.count.Count
import scala.math.Numeric
import scala.util.Try

trait CountupGenerate[T] extends Generate[T] {

  def counter: Count[T]

  def generate = Try(counter.incr)
}

object CountupGenerate {
  def apply[T: Numeric](): CountupGenerate[T] = new CountupGenerate[T] {
    val counter = Count[T]()
  }
}
