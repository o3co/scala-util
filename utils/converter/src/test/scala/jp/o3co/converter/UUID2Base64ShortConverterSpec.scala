package jp.o3co.converter

import org.specs2.mutable.Specification
import java.util.UUID

class UUID2Base64ShortConverterSpecs extends Specification {
 
  "UUID2Base64ShortConverter" should {
    "convert UUID to base64 string" in {
      val uuid = UUID.fromString("B606FC11-8DF7-4E0A-A957-35038002D9F7")

      val converter6 = new UUID2Base64ShortConverter(6)
      "tgb8EY" === converter6.convert(uuid)

      val converter8 = new UUID2Base64ShortConverter(8)
      "tgb8EY33" === converter8.convert(uuid)
    }
  }
}

