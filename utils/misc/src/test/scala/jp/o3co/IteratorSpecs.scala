package jp.o3co.iterator

import org.specs2.mutable.Specification

class IteratorSpecs extends Specification {
 
  "NextOptionIterator" should {
    "wrap next with Option" in {
      val itr = Seq(1, 2).iterator
      Some(1) === itr.nextOption
      Some(2) === itr.nextOption()
      None === itr.nextOption()
    }
    "return default if no more next exists" in {
      val itr = Seq(1, 2).iterator
      1 === itr.nextOrElse(5)
      2 === itr.nextOrElse(5)
      5 === itr.nextOrElse(5)
    }
  }
}

