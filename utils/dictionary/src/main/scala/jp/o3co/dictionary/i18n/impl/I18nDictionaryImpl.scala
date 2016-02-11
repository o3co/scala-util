package jp.o3co.dictionary
package i18n
package impl

import multi._
import multi.impl._

import java.util.Locale
import collection.generic.CanBuildFrom
import scala.collection.mutable


trait BaseI18nDictionaryImpl[K, V] extends BaseMultiDictionaryImpl[Locale, K, V] 
  with I18nDictionary[K, V]
{

  def localeSet = keySet.toSet

  def locales   = keys

}

trait I18nDictionaryImplLike[K, V, +This <: I18nDictionaryImplLike[K, V, This] with BaseI18nDictionaryImpl[K, V]] 
  //extends MultiDictionaryImplLike[Locale, Dictionary[K, V], This] 
  extends MultiDictionaryImplLike[Locale, K, V, This] 
  with AutoExpandableMultiDictionaryLike[Locale, K, V] 
{
  this: BaseI18nDictionaryImpl[K, V] => 

  override def dictionary(locale: Locale): SubDictionary
    = super[AutoExpandableMultiDictionaryLike].dictionary(locale)

  override protected def expandWith(selector: Locale, newDictionary: SubDictionary) {
    putDictionary(selector, newDictionary) 
  }
}

class I18nDictionaryImpl[K, V] extends BaseI18nDictionaryImpl[K, V] 
  with I18nDictionaryImplLike[K, V, I18nDictionaryImpl[K, V]] 
{
  override def empty: I18nDictionaryImpl[K, V] = new I18nDictionaryImpl[K, V]

  override def apply(locale: Locale) = dictionary(locale)
}

object I18nDictionaryImpl {
  
  def empty[K, V] = new I18nDictionaryImpl[K, V]
      
  def apply[K, V](kvs: (Locale, Dictionary[K, V])*): I18nDictionaryImpl[K, V] = {
    val m: I18nDictionaryImpl[K, V] = empty
    for (kv <- kvs) m += kv
    m
  }

  //def newBuilder[K, V]: Builder[(K, V), I18nDictionaryImpl[A, B]] = 
  //  new MapBuilder[A, B, I18nDictionaryImpl[A, B]](empty)
  //        
  //implicit def canBuildFrom[A, B]: CanBuildFrom[I18nDictionaryImpl[_, _], (A, B), I18nDictionaryImpl[A, B]] = 
  //  new CanBuildFrom[I18nDictionaryImpl[_, _], (A, B), I18nDictionaryImpl[A, B]] {
  //    def apply(from: I18nDictionaryImpl[_, _]) = newBuilder[A, B]
  //    def apply() = newBuilder[A, B]
  //  }
}
