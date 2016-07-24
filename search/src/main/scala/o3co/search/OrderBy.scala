package o3co.search

sealed trait OrdinalDirection
object OrdinalDirections {
  case object ASC extends OrdinalDirection
  case object DESC extends OrdinalDirection
}

/**
 *
 */
sealed trait OrderBy {
  def asc: OrderBy
  def desc: OrderBy
}

object OrderBy {
  /**
   *
   */
  trait ImplicitConversions extends Any {
    import scala.language.implicitConversions
  
    /**
     *
     */
    implicit def stringToOrderBy(notation: String): OrderByField = OrderByField(notation)
  }

  object ImplicitConversions extends ImplicitConversions 
}

case class OrderByField(fieldname: String, direction: OrdinalDirection = OrdinalDirections.ASC) extends OrderBy {
  def asc: OrderByField = copy(direction = OrdinalDirections.ASC)
  def desc: OrderByField = copy(direction = OrdinalDirections.DESC)
}

case class OrderByFields(fields: Seq[OrderByField] = Seq(), direction: OrdinalDirection = OrdinalDirections.ASC) extends OrderBy {
  def asc: OrderByFields = copy(direction = OrdinalDirections.ASC)
  def desc: OrderByFields = copy(direction = OrdinalDirections.DESC)
}

//
object OrderByField {
  val WithPlus  = "(.*)\\+".r
  val WithMinus = "(.*)\\-".r
  val WithAsc   = "(.*) asc".r
  val WithDsc   = "(.*) dsc".r
  val WithDesc  = "(.*) desc".r

  def apply(notation: String): OrderByField = {
    notation match {
      case WithPlus(field)  => asc(field)
      case WithMinus(field) => desc(field)
      case WithAsc(field)   => asc(field)
      case WithDsc(field)   => desc(field)
      case WithDesc(field)  => desc(field)
      case _ => asc(notation)
    }
  }

  def asc(field: String) = new OrderByField(field, OrdinalDirections.ASC)

  def desc(field: String) = new OrderByField(field, OrdinalDirections.DESC)
}

trait OrderByFactory {
  /**
   * {{{
   *   OrderByFactory.orderBy("field", "field" desc)
   *
   * }}}
   */
  def orderBy(fields: OrderByField *): OrderBy = {
    if(fields.size == 1) fields.head
    else OrderByFields(fields)
  }
}

/**
 * Trait of Factory to create ordering of type T
 */
trait OrderingFactory[T] extends OrderByFactory {
  import o3co.math.PriorityOrdering
  import scala.language.implicitConversions

  case class NoneOrdering() extends Ordering[T] {
    def compare(x: T, y: T): Int = 0
  }

  /**
   * Factory method of ordering
   */
  implicit def ordering(orderBy: OrderBy): Ordering[T] = orderBy match {
    case OrderByField(fieldname, direction) => 
      direction match {
        case OrdinalDirections.ASC  => fieldOrderings(fieldname)
        case OrdinalDirections.DESC => fieldOrderings(fieldname).reverse
      }
    case OrderByFields(fields, direction) => 
      direction match {
        case OrdinalDirections.ASC  => PriorityOrdering(fields.map(ordering(_)): _*)
        case OrdinalDirections.DESC => PriorityOrdering(fields.map(ordering(_)):_*).reverse
      }
  }

  /**
   * PartialFunction to get specified field ordering
   */
  def fieldOrderings: PartialFunction[String, Ordering[T]] = propertyOrderings orElse noneFieldOrdering

  val noneFieldOrdering: PartialFunction[String, Ordering[T]] = {
    case _ => NoneOrdering()
  }

  /**
   * {{{
   *   val fieldOrdering = {
   *     case "field1" => Ordering.by(v => v.field1)
   *     case "field2" => Ordering.by(v => v.field2)
   *   } 
   * }}}
   */
  def propertyOrderings: PartialFunction[String, Ordering[T]] = PartialFunction.empty
}

