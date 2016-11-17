package o3co.store.rediscala

import o3co.store.kvs.KeyValueStore
import o3co.store.vs.ValueStore
import redis.RedisCommands 
import redis.commands.TransactionBuilder
import redis.commands.Transactions
import scala.concurrent.ExecutionContext

/**
 *
 */
trait RedisStoreImpl[Key] {
  def redis: RedisCommands with Transactions 

  implicit def executionContext: ExecutionContext

  def withTransaction(key: String)(f: TransactionBuilder => Unit) = {
    val transaction = redis.transaction()
    transaction.watch(key)

    f(transaction)

    transaction.exec()
  }
}


