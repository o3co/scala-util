package jp.o3co.aws
package config

import com.typesafe.config.Config
import jp.o3co.config.GlobalSettings
import jp.o3co.config.Settings

trait AWSSettingsLike {
  this: Settings => 
  
  def hasRegion: Boolean = config.hasPath("region")

  def hasCredentials: Boolean = config.hasPath("credentials")

  def region: Region = Region(config.getString("region"))

  def credentials: Credentials = Credentials(config.getString("access-key"), config.getString("secret-key"))
}

trait AWSEndpointSettingsLike extends AWSSettingsLike {
  this: Settings => 


  /**
   * Get Endpoint (which describe the domain of the endpoint) from AWSService.
   * @param service Service of the instance
   */
  def endpointFor(service: AWSService): Endpoint = 
    endpointFor(service.name)

  /**
   * Get Endpoint (which describe the domain of the endpont)
   *
   * @param serviceName Service name of the instance. 
   */
  def endpointFor(serviceName: String): Endpoint = {
    if(config.hasPath("path")) PathEndpoint(config.getString("path"))
    else ServiceEndpoint(serviceName, config.getString("name"), region)
  }

  /**
   * Get credentials for the enpoint settings
   */
  override lazy val credentials = 
    if(hasCredentials) super.credentials
    else AWSGlobalSettings.defaultCredentials
}

/**
 * Wrapper settings of Config as AWSEndpointSettingsLike 
 */
case class AWSEndpointSettings(config: Config) extends Settings with AWSEndpointSettingsLike 
