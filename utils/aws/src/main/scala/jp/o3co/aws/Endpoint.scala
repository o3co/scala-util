package jp.o3co.aws

/**
 * Endpoint of the Service
 */
trait Endpoint {
  def path: String
}

/**
 * Endpoint which automatically generate domain path from service, instance name and region.
 */
case class ServiceEndpoint(serviceName: String, name: String, region: Region) extends Endpoint {

  val suffix: String = "amazonaws.com"

  def path: String = s"$name.$region.$serviceName.$suffix"
}

/**
 * Endpoint which described as Path
 */
case class PathEndpoint(path: String) extends Endpoint 

object PathEndpoint {
  import com.typesafe.config.Config

  /**
   * Create PathEndpoint from configuration
   */
  def apply(config: Config) = new PathEndpoint(config.getString("path"))
}
