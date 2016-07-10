package o3co.store.slick

import o3co.store.entity._

trait SlickEntityStoreImpl[K, E <: Entity[K]] extends EntityStore[K, E] with SlickEntityStoreLike[K, E] 

trait SlickEntityStoreLike[K, E <: Entity[K]] extends EntityStoreLike[K, E] with SlickStoreLike {
  this: EntityStore[K, E] =>

  import profile.api._

  implicit def idColumnType: ColumnType[K]

  abstract class EntityTable(tag: Tag, name: String) extends Table[E](tag, name) {
    def id      = column[K]("id", O.PrimaryKey)
  }

  type Entities <: EntityTable

  def entities: TableQuery[Entities]

  def tables = Seq(entities)

  def countAsync = {
    database.run(entities.length.result)
      .map(_.toLong)
  }

  def existsAsync(id: K) = {
    database.run(entities.filter(_.id === id).size.result)
      .map (_ > 0)
  }

  def keysAsync() = {
    database.run(entities.map(_.id).result)
      .map(_.toSet)
  }

  override def getAsync(id: K) = {
    database.run(entities.filter(_.id === id).result.headOption)
  }

  def getAsync(ids: K *) = {
    val order = ids.zipWithIndex.toMap
    database.run(entities.filter(_.id inSet ids.toSet).result)
      .map { es => es.sortBy(e => order(e.id)) }
  }

  def getAsync(ids: Set[K]) = {
    database.run(
      entities
        .filter(_.id inSet ids)
        .result
    ).map(_.map(e => (e.id, e)).toMap)
  }

  def deleteByIdAsync(ids: K *) = {
    database.run(
      if(ids.size > 1) DBIO.sequence(ids.map(id => entities.filter(_.id === id).delete))
      else this.entities.filter(_.id === ids.head).delete
    )
      .map(_ => (): Unit)
  }

  def addAsync(entities: E *) = {
    database.run(
      if(entities.size > 1) DBIO.sequence(entities.map(e => this.entities += e))
      else this.entities += entities.head
    )
      .map(_ => (): Unit)
  }

  //def addAsync(entity: E) = {
  //  database.run(
  //    entities += entity
  //  )
  //    .map(_ => (): Unit)
  //}

  def putAsync(entities: E *) = {
    database.run(
      if(entities.size > 1) DBIO.sequence(entities.map(e => this.entities.insertOrUpdate(e)))
      else this.entities.insertOrUpdate(entities.head)
    )
      .map(_ => (): Unit)
  }

  //def putAsync(entity: E) = {
  //  database.run(
  //    entities.insertOrUpdate(entity) 
  //  )
  //    .map(_ => (): Unit)
  //}

  def valuesAsync() = {
    database.run(entities.result)
  }
}

