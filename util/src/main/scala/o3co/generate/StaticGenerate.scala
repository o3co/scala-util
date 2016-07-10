package o3co.generate

import scala.util.Try

trait StaticGenerate[T] extends Generate[T] {

  def value: T

  def generate = Try(value)
} 

object StaticGenerate {
  def apply[T](v: T) = new StaticGenerate[T] {
    val value = v
  }
}
