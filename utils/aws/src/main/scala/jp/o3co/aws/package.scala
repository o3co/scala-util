package jp.o3co

import com.amazonaws.{regions => awsregions}
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.BasicSessionCredentials

package object aws {
  type Region = com.amazonaws.regions.Region
  val  Region = RegionFactory

  object RegionFactory {
    private var defaultRegion: Region = awsregions.Region.getRegion(awsregions.Regions.DEFAULT_REGION)

    def default() = defaultRegion
    def default(region: awsregions.Region) = defaultRegion = region

    def apply(region: awsregions.Regions): Region = awsregions.Region.getRegion(region)

    def apply(name: String): Region = apply(awsregions.Regions.fromName(name.toLowerCase.replace('_', '-')))

    val GovCloud       = apply(awsregions.Regions.GovCloud)
    val US_EAST_1      = apply(awsregions.Regions.US_EAST_1)
    val US_WEST_1      = apply(awsregions.Regions.US_WEST_1)
    val US_WEST_2      = apply(awsregions.Regions.US_WEST_2)
    val EU_WEST_1      = apply(awsregions.Regions.EU_WEST_1)
    val EU_CENTRAL_1   = apply(awsregions.Regions.EU_CENTRAL_1)
    val AP_SOUTHEAST_1 = apply(awsregions.Regions.AP_SOUTHEAST_1)
    val AP_SOUTHEAST_2 = apply(awsregions.Regions.AP_SOUTHEAST_2)
    val AP_NORTHEAST_1 = apply(awsregions.Regions.AP_NORTHEAST_1)
    val AP_NORTHEAST_2 = apply(awsregions.Regions.AP_NORTHEAST_2)
    val SA_EAST_1      = apply(awsregions.Regions.SA_EAST_1)
    val CN_NORTH_1     = apply(awsregions.Regions.CN_NORTH_1)
  }

  object CredentialsFactory {
    import com.typesafe.config.Config

    def apply(accessKey: String, secretKey: String) = new BasicAWSCredentials(accessKey, secretKey)
    def apply(accessKey: String, secretKey: String, token: String) = new BasicSessionCredentials(accessKey, secretKey, token)

    def apply(config: Config): AWSCredentials = 
      apply(config.getString("access_key"), config.getString("secret_key"))
  }

  type Credentials = com.amazonaws.auth.AWSCredentials

  val Credentials = CredentialsFactory

  case class Size(value: Long) {
    def toLong: Long = value 

    def toInt: Int   = value.toInt

    def toDouble: Double = value.toDouble
  }
}
