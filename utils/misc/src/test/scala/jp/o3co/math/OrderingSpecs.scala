package jp.o3co.math

import org.specs2.mutable.Specification
import scala.math.Ordering

class PriorityOrderingSpecs extends Specification {
 
  "PrioirtyOrdering" should {
    "order properly" in {
      val o1: Ordering[(Int, Int)] = Ordering.by(n => n._1)
      val o2: Ordering[(Int, Int)] = Ordering.by(n => n._2)

      implicit val ordering: Ordering[(Int, Int)] = PriorityOrdering(o1, o2.reverse)

      val values = Seq((1, 2), (1, 3), (1, 1), (2, 1), (2, 2))
      Seq((1, 3), (1, 2), (1, 1), (2, 2), (2, 1)) === values.sorted
      Seq((2, 1), (2, 2), (1, 1), (1, 2), (1, 3)) === values.sorted(ordering.reverse)
    }
  }
}


