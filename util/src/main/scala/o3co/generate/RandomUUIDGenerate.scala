package o3co.generate

import java.util.UUID
import scala.util.Try

trait RandomUUIDGenerate extends Generate[UUID] {
  
  def generate = Try(UUID.randomUUID())
}

object RandomUUIDGenerate {
  def apply() = new RandomUUIDGenerate {}
}
