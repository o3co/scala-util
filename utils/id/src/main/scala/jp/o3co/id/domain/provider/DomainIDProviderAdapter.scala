package jp.o3co.id.domain.provider

import jp.o3co.id.provider._
import jp.o3co.net.DomainName
import scala.concurrent.Future

/**
 *
 */
trait DomainIDProviderAdapterLike[ID] extends IDProviderAdapterLike[ID] with impl.DomainIDProviderImpl[ID] {

  /*
   */
  override def existsAsync(id: ID) = rootDomain match {
    case Some(root) => doExistsAsync(id, root)
    case None       => doExistsAsync(id)
  }

  /*
   */
  override def generateAsync() = rootDomain match {
    case Some(root) => doGenerateAsync(root)
    case None       => doGenerateAsync()
  }

  /*
   */
  override def releaseAsync(id: ID) = doReleaseAsync(id, rootDomain)
}
