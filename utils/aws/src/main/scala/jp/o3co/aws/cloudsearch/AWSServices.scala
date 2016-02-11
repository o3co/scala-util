package jp.o3co.aws

/**
 * Helper trait to choose service name
 */
sealed abstract class AWSService(val name: String) 

/**
 * Default definition of the services
 */
object AWSServices {
  case object CloudSearch extends AWSService("cloudsearch")
}
