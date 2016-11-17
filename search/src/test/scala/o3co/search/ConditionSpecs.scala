package o3co.search

import org.specs2.mutable.Specification

class ConditionSpec extends Specification {
  
  case class Hoge()

  "Any" should {
    "be applied to any type of condition" in {
      Condition.Any must beAnInstanceOf[Condition[Int]]
      Condition.Any must beAnInstanceOf[Condition[String]]
      Condition.Any must beAnInstanceOf[Condition[Double]]
      Condition.Any must beAnInstanceOf[Condition[Hoge]]
    }
  }
}
