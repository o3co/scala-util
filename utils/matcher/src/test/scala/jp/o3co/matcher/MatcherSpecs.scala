package jp.o3co.matcher

import org.specs2.mutable.Specification
import jp.o3co.matcher.Matcher._

class MatcherSpecs extends Specification {
 
  "Matcher" should {
    "be created from function T => Boolean" in {
      val matcher: Matcher[Int] = Matcher {
        case n if 0 == n % 2 => Matched(n)
        case _ => Unmatched("Not matched")
      }

      matcher(1) === Unmatched()
      matcher(2) === Matched(2)
    }
    "convert to another type matcher by" in {
      val stringMatcher: Matcher[String] = Matcher(value => Matched(value))

      val matcher: Matcher[Int] = stringMatcher.by(value => value.toString)

      matcher(1) === Matched(1)
    }
    "matcher can match with property with by method" in {
      case class Item(name: String)

      val stringMatcher: Matcher[String] = Matchers.Equals("test")
      val propertyMatcher: Matcher[Item] = stringMatcher.by[Item](_.name)
      val item = Item("test")

      propertyMatcher(item) === Matched(Item("test"))
    }
  }
}

