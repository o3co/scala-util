package jp.o3co.net

import org.specs2.mutable.Specification

class NetSpecs extends Specification {
 
  "DomainName" should {
    "validate the name on construct" in {
      // TopDomain Only
      DomainName("com") must beAnInstanceOf[DomainName]
      // Numeric Domain
      DomainName("123.jp") must beAnInstanceOf[DomainName]
      // long domain
      DomainName("dubdomain.parentdomain.rootdomain.jp") must beAnInstanceOf[DomainName]

      // LessResctrict 
      DomainName("a.b") must beAnInstanceOf[DomainName]

      // Cannot start with "-"
      DomainName("-test.com") must throwA[IllegalArgumentException]
      // Cannot contains "*"
      DomainName("test*.com") must throwA[IllegalArgumentException]
    }
    "validate the relation" in {
      
      val com = DomainName("com")
      val comSample = DomainName("com.sample")

      comSample.isSubdomainOf(com) === true
    }
  }
}
