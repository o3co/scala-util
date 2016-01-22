package jp.o3co.util.counter.impl

import org.specs2.mutable.Specification

class CounterImplSpec extends Specification {

  "CounterImpl" should {
    "increment/decrement values" in {
      val counter = CounterImpl[Int]()

      0 === counter.count

      1 === counter.incr
      2 === counter.increment()
      4 === counter.incr(2)
      6 === counter.increment(2)
      7 === counter.increment(None)

      counter + 1
      8 === counter.count

      counter - 1
      7 === counter.count

      6 === counter.decr
      5 === counter.decrement()
      3 === counter.decr(2)
      1 === counter.decrement(2)
      0 === counter.decrement(None)
    }
    "rest" in {
      val counter = CounterImpl[Int]()

      counter.rest(6)

      6 === counter.count

      counter.rest()
      0 === counter.count
    }
  }
}
