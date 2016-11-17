package o3co.store.slick

import o3co.store.kvs.KeyValueStore

trait SlickKeyValueStoreImpl[K, V] extends KeyValueStore[K, V] with SlickKeyValueStoreLike[K, V] {

  import profile.api._

  val tableName: String

  class KeyValues(tag: Tag) extends KeyValueTable(tag, tableName)

  def keyValues = TableQuery[KeyValues]
}

trait SlickKeyValueStoreLike[K, V] extends SlickStoreLike {
  this: KeyValueStore[K, V] =>

  import profile.api._

  implicit def keyColumnType: ColumnType[K]
  implicit def valueColumnType: ColumnType[V]

  /**
   */
  abstract class KeyValueTable(tag: Tag, name: String) extends Table[(K, V)](tag, name) {

    val keyColumnName = "`key`"
    val valueColumnName = "`value`"

    def key   = column[K](keyColumnName, O.PrimaryKey)
    def value = column[V](valueColumnName)

    def * = (key, value)
  }

  import scala.language.existentials

  def keyValues: TableQuery[_ <: KeyValueTable]

  def tables = Seq(keyValues)

  def countAsync = {
    database.run(keyValues.length.result)
      .map(_.toLong)
  }
  
  def existsAsync(key: K) = {
    database.run(keyValues.filter(_.key === key).size.result)
      .map (_ > 0)
  }
  
  def keysAsync = {
    database.run(keyValues.map(_.key).result).map(_.toSet)
  }

  override def getAsync(key: K) = {
    database.run(keyValues.filter(_.key === key).map(_.value).result.headOption)
  }

  def getAsync(keys: K *) = {
    database.run(
      keyValues
        .filter(_.key inSet keys.toSet)
        .map(_.value)
        .result
      )
  }

  def getAsync(keys: Set[K]) = {
    database.run(
      keyValues
        .filter(_.key inSet keys)
        .result
    ).map(_.toMap)
  }

  def setAsync(key: K, value: V) = {
    database.run(
      keyValues.insertOrUpdate((key, value))
    )
      .map(_ => (): Unit)
  }

  def setAsync(kvs: Map[K, V]) = {
    database.run(
      DBIO.sequence(kvs.map { kv =>
        keyValues.insertOrUpdate(kv)
      })
    )
      .map(_ => (): Unit)  
  }

  def addAsync(key: K, value: V) = {
    database.run(
      keyValues += (key, value)
    )
      .map(_ => (): Unit)
  }

  def addAsync(kvs: Map[K, V]) = {
    database.run(
      DBIO.sequence(kvs.map { kv =>
        keyValues += kv
      })
    )
      .map(_ => (): Unit)  
  }

  def deleteAsync(keys: K *) = {
    database.run(
      keyValues.filter(_.key inSet keys.toSet).delete
    )
      .map(_ => (): Unit)
  }
}

