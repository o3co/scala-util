package o3co.store.entity

import o3co.store
import o3co.conversion._
import scala.concurrent.ExecutionContext

/**
 */
trait ReadAccessMapping[ToID, FromID, ToEntity <: Entity[ToID], FromEntity <: Entity[FromID]] extends ReadAccessLike[ToID, ToEntity] with store.kvs.ReadAccessMapping[ToID, FromID, ToEntity, FromEntity] {
  this: ReadAccess[ToID, ToEntity] =>

  def endpoint: ReadAccess[FromID, FromEntity]

  def valueConverter: Conversion[FromEntity, ToEntity]

  def modelConverter = valueConverter
}

/**
 */
trait WriteAccessMapping[ToID, FromID, ToEntity <: Entity[ToID], FromEntity <: Entity[FromID]] extends WriteAccessLike[ToID, ToEntity] with store.vs.WriteAccessMapping[ToEntity, FromEntity] {
  this: WriteAccess[ToID, ToEntity] =>

  def endpoint: WriteAccess[FromID, FromEntity]

  def valueConverter: ReversibleConversion[FromEntity, ToEntity]

  implicit def keyConverter: ReversibleConversion[FromID, ToID]

  implicit def reverseKeyConverter = keyConverter.reverse

  def modelConverter = valueConverter

  def idConverter = keyConverter

  def deleteByIdAsync(ids: ToID *) = 
    endpoint.deleteByIdAsync(ids.map(id => id: FromID): _*)

  override def deleteAsync(entities: ToEntity *) = 
    deleteByIdAsync(entities.map(_.id): _*)
}

/**
 */
trait ReadOnlyEntityStoreMapping[ToID, FromID, ToEntity <: Entity[ToID], FromEntity <: Entity[FromID]] extends store.ProxyStore with ReadOnlyEntityStore[ToID, ToEntity] with ReadAccessMapping[ToID, FromID, ToEntity, FromEntity] {

  def endpoint: store.Store with ReadAccess[FromID, FromEntity]
}

/**
 * EntityStoreMapping[ToID, Entity, FromEntity] is a Proxy EntityStore which implements EntityStore[ToID, FromEntity]
 * and has undelrying EntityStore[ToID, Entity] with conversion Entity => FromEntity, FromEntity => Entity
 *
 */
trait EntityStoreMapping[ToID, FromID, ToEntity <: Entity[ToID], FromEntity <: Entity[FromID]] extends store.ProxyStore with EntityStore[ToID, ToEntity] with ReadOnlyEntityStoreMapping[ToID, FromID, ToEntity, FromEntity] with WriteAccessMapping[ToID, FromID, ToEntity, FromEntity] {

  def endpoint: store.Store with ReadAccess[FromID, FromEntity] with WriteAccess[FromID, FromEntity]

  def valueConverter: ReversibleConversion[FromEntity, ToEntity]

  override def reverseKeyConverter = keyConverter.reverse

  override def modelConverter = valueConverter

  override def idConverter = keyConverter
}

