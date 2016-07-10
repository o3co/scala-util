package o3co.matcher

trait TraversableMatcher[T <: Traversable[_]] extends Matcher[T]

trait SetMatcher[T] extends TraversableMatcher[Set[T]]

trait MapMatcher[K, V] extends TraversableMatcher[Map[K, V]]

trait SeqMatcher[T] extends TraversableMatcher[Seq[T]]
