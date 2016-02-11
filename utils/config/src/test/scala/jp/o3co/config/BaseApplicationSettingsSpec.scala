package jp.o3co.config

import org.specs2.mutable.Specification
import java.util.Locale
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigValueType
import scala.concurrent.duration._

class BaseApplicationSettingsSpec extends Specification with BaseApplicationSettings {
  
  def config = ConfigFactory.empty

  "BaseApplicationSettings" should {
    "parse Map values as a config" in {

      val c = parseMap(Map(
        "string" -> Some("Foo"),
        "int" -> Some(1),
        "none" -> None
      ))

      c must beAnInstanceOf[Config]
      c.hasPath("string") === true
      c.getValue("string").valueType === ConfigValueType.STRING 
      c.getString("string") === "Foo"

      c.hasPath("int") === true
      c.getValue("int").valueType === ConfigValueType.NUMBER
      c.getInt("int") === 1

      c.hasPath("none") === false 
    }
  }
}

