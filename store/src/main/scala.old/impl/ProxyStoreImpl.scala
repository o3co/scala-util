package o3co.store

/**
 * Base implementation of ProxyStore 
 */
trait ProxyStoreLike extends StoreLike {
  this: Store => 

  def endpoint: Store 

  /**
   */
  def countAsync = endpoint.countAsync
}

trait ProxyStore extends Store with ProxyStoreLike
