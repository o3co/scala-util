package jp.o3co.search

import org.specs2.mutable.Specification
import jp.o3co.math.PriorityOrdering

class TestOrderingFactorySpecs extends Specification {
 
  case class Item(id: Int, name: String)
  object Item extends OrderingFactory[Item] {
    override val fieldOrderings: PartialFunction[String, Ordering[Item]] = {
      case "id"    => Ordering.by[Item, Int](item => item.id)
      case "name"  => Ordering.by[Item, String](item => item.name)
    }
  }
  
  "OrderingFactory" should {
    "create Ordering from OrderBy" in {
      import jp.o3co.search.ImplicitConversions._
      val items = Seq(Item(2, "bbb"), Item(2, "aaa"), Item(3, "aaa"), Item(1, "bbb"))
      val orderBy = Item.orderBy("id", "name" desc)
      orderBy must beAnInstanceOf[OrderByFields]
      val ordering = Item.ordering(orderBy)
      ordering must beAnInstanceOf[PriorityOrdering[Item]]
      Seq(Item(1, "bbb"), Item(2, "bbb"), Item(2, "aaa"), Item(3, "aaa"))=== items.sorted(ordering)
    }
  }
}


