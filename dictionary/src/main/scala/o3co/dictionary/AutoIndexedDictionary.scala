package o3co.dictionary

import o3co.generate._
/**
 * 
 */
trait AutoIndexedDictionaryLike[K, V] {
  this: Dictionary[K, V] =>
  
  import scala.util._
  def generator: Generate[K]

  def put(value: V): K = {
    generator().map { key => 
      put(key, value)
      key
    }.get
  }
}
