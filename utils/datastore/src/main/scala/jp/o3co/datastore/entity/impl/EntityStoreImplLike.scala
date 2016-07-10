package jp.o3co.datastore
package entity
package impl

import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.ExecutionContext

trait BaseEntityStoreImpl[K, E <: BaseEntity[K]] extends mutable.Map[K, E]

trait EntityStoreImplLike[K, E <: BaseEntity[K], +This <: EntityStoreImplLike[K, E, This] with BaseEntityStoreImpl[K, E]] extends mutable.MapLike[K, E, This] with EntityStore[K, E] {

  implicit def executionContext: ExecutionContext

  var entities: Map[K, E] = Map()

  def get(key: EntityKey): Option[Entity] = entities.get(key)

  def iterator = entities.iterator

  def +=(v: Entity): this.type = {
    this += (v.entityKey -> v)
    this
  }

  def +=(kv: (EntityKey, Entity)): this.type = {
    entities = entities + kv
    this
  }

  def -=(key: EntityKey): this.type = {
    entities = entities - key
    this
  }

  def countEntitiesAsync = Future.successful(entities.size)

  def entityExistsAsync(key: EntityKey) = {
    val exists = contains(key)
    Future.successful(exists)
  }

  def getKeysAsync() = {
    Future.successful(entities.keys.toSeq)
  }
  
  def getEntityAsync(key: EntityKey) = {
    val entity = entities.get(key)
    Future.successful(entity)
  }

  def getEntitiesAsync(keys: Seq[EntityKey]) = {
    Future.successful(keys.collect { 
      case key if entities.contains(key) => entities(key)
    })
  }

  def putEntityAsync(entity: Entity) = {
    val prev = put(entity.entityKey, entity)
    Future.successful((): Unit)
  }

  def deleteEntityAsync(key: EntityKey) = {
    val deleted = remove(key)
    Future.successful((): Unit)
  }
}

class EntityStoreImpl[K, E <: BaseEntity[K]](implicit val executionContext: ExecutionContext) extends BaseEntityStoreImpl[K, E] with EntityStoreImplLike[K, E, EntityStoreImpl[K, E]] {

  override def empty = new EntityStoreImpl[K, E]
}

object EntityStoreImpl {
  def apply[K, V <: BaseEntity[K]](implicit ec: ExecutionContext) = new EntityStoreImpl[K, V]
}
