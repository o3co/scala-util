package jp.o3co.boxing

class Box[A](val underlying: A) {
  def unwrapped: A = underlying

  def isEmpty: Boolean = underlying == null

  def isDefined: Boolean = underlying != null

  override def toString: String = underlying.toString

  override def equals(other: Any): Boolean = other match {
    case b: Box[A] => b.unwrapped == underlying
    case _ => false
  }
}

object Box {
  def apply[T](value: T) = new Box(value)
}

