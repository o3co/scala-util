package jp.o3co.routing

case class DelegateType(_name: String) {
  /**
   *
   */
  override def equals(other: Any): Boolean = other match {
    case t: DelegateType => t.name == name
    case n: String => n.toLowerCase == name
    case _ => false
  }

  def name: String = _name.toLowerCase 

  override def toString: String = name
}

