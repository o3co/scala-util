package jp.o3co.aws
package cloudsearch
package query

import org.specs2.mutable.Specification

class QueryOptionSpec extends Specification {

  import ImplicitConversions._

  "QueryOptions" should {
    "be like a Map[String, QueryOption]" in {
      val options = QueryOptions()

      val updated = options + DefaultOperator.OR + DefaultOperator.AND + Fields("foo", "bar")

      updated.size === 2

      updated.contains("defaultOperator") === true
      updated.contains("fields") === true

      updated("fields") === Fields("foo", "bar")
    }
  }

}
