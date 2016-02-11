package jp.o3co.net

import org.specs2.mutable.Specification

class NetSpecs extends Specification {
 
  "DomainName" should {
    "validate the name on construct" in {
      DomainName("something.com") must beAnInstanceOf[DomainName]
      DomainName("123.jp") must beAnInstanceOf[DomainName]
      DomainName("dubdomain.parentdomain.rootdomain.jp") must beAnInstanceOf[DomainName]

      // Cannot start with "-"
      DomainName("-test.com") must throwA[IllegalArgumentException]
      // Cannot contains "*"
      DomainName("test*.com") must throwA[IllegalArgumentException]
    }
  }
}
