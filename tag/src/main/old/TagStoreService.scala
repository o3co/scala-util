package o3co.tag
package store

trait TagExpression extends Any
object TagExpression {
  val TagNamePattern = "^([a-zA-Z0-9_\\.]+)$"
  val TagPrefixPattern = "^([a-zA-Z])*$"

  def apply(literal: String) = literal match {
    case TagNamePattern(name) = TagName(name)
    case TagPrefixPattern(name) = TagNamePrefix(name)
    case TagPostfixPattern(name) = TagNamePostfix(name)
  }
}

/**
 * Fully Quallified Tagname
 */
case class TagName(underlying: String) extends AnyVal with TagExpression 
 
/**
 * Tag prefix
 */
case class TagNamePrefix(underlying: String) extends AnyVal with TagExpression

/**
 *
 */
case class TagNamePostfix(underlying: String) extends AnyVal with TagExpression

/**
 *
 */
trait TagStoreService[ID] extends ValueStore[(ID, TagName)] {

  def containsTagAsync(id: ID, tag: String): Future[Boolean] = 
    containsTagAsync(id, TagName(tag))

  def containsTagAsync(id: ID, tag: TagName): Future[Boolean] 

  def getTagsAsync(id: ID): Set[TagName]
  
  def addTagAsync(id: ID, tags: String *): Future[Unit] = 
    addTagAsync(id, tags.map(TagName(_)))

  def addTagAsync(id: ID, tags: TagName *): Future[Unit]

  def removeTagAsync(id: ID, tags: String *): Future[Unit] =
    removeTagAsync(id, tags.map(name => TagName(name)))

  def removeTagAsync(id: ID, tags: TagName *): Future[Unit]

  /**
   * Replace tags with newTags.
   * If oldTags is not specified, then replace all tags with the id to newTags.
   * Otherwise, replace oldTags to newTags
   */
  def replaceTagAsync(id: ID, newTags: Set[TagName], oldTags: Option[Set[TagName]] = None)
}

/**
 *
 */
trait StoredTagStoreServiceImpl[ID] extends TagStoreService[ID] {

  def store: DALStore 

  def containsTagAsync(id: ID, tag: TagName): Future[Boolean] = 
    store.existsAsync((id, tag))

  def getTagsAsync(id: ID): Set[TagName] =
    store.findAsync(TagEntityCondition(id = Equals(id)))

  def addTagAsync(id: ID, tags: TagName *): Future[Unit] =
    store.putAsync(tags.map(name => (id, name)))

  def removeTagAsync(id: ID, tags: TagName *): Future[Unit] = 
    store.deleteAsync(tags.map(tag => (id, tag)))
}

case class EntityCondition[ID](id: Option[Condition[ID]], name: Option[Condition[TagName]])

object EntityCondition {
  def apply[ID](
    id: Condition[ID] = null,
    name: Condition[TagName] = null
  ) = {
    new EntityCondition(Option(id), Option(name))
  }
}

