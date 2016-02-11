package jp.o3co.converter

import org.specs2.mutable.Specification
import java.util.UUID

class UUID2Base64ConverterSpecs extends Specification {
 
  "UUID2Base64Converter" should {
    "convert UUID to base64 string" in {
      val uuid = UUID.fromString("B606FC11-8DF7-4E0A-A957-35038002D9F7")

      val converter = new UUID2Base64Converter()
      "tgb8EY33TgqpVzUDgALZ9w" === converter.convert(uuid)

      val converter2 = new UUID2Base64Converter(false)
      "tgb8EY33TgqpVzUDgALZ9w==" === converter2.convert(uuid)
    }
  }
}

