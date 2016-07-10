package jp.o3co.id.store
package impl

/**
 *
 */
trait MySQLIDStoreImpl[ID] extends SlickIDStoreImpl[ID] {
  
  val profile = slick.driver.MySQLDriver
}
