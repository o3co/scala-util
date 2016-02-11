package jp.o3co.search
package condition

import scala.util.parsing.combinator.RegexParsers
import scala.util.parsing.input.CharSequenceReader

trait ConditionParser {
  def parse[T](notation: String): Condition[T]
}

case class Value(value: String)


class DefaultConditionParser extends RegexParsers with ConditionParser {
  import scala.language.postfixOps
  // Operator on LEFT 
  def eq    = "="  | "eq"
  def lt    = ">"  | "lt"
  def le    = ">=" | "le"
  def gt    = "<"  | "gt" 
  def ge    = "<=" | "ge"
  def ne    = "!"  | "ne"

  def not   = "not"
  def and   = "and"
  def or    = "or"
  def in    = "in"
  def range = "range"
  
  def logicalExpression = (and ^^^ Operators.AND | or ^^^ Operators.OR) ~ "(" ~ expressionList ~ ")" ^^ { 
    case Operators.AND ~ _ ~ expr ~ _ => And(expr: _*) 
    case Operators.OR ~ _ ~ expr ~ _ => Or(expr: _*) 
  }

  def inExpression = "in(" ~ valueList ~ ")" ^^ { case _ ~ v ~ _ => Contains(v) }
  def simpleComparisonExpression = value ^^ { case v => Equals(v) }
  def opComparisonExpression = comparator ~ value ^^ { 
    case Operators.Equals ~ v           => Equals(v) 
    case Operators.NotEquals ~ v        => NotEquals(v) 
    case Operators.LessThan ~ v         => LessThan(v) 
    case Operators.LessThanOrEquals ~ v => LessThanOrEquals(v) 
    case Operators.GreaterThan ~ v      => GreaterThan(v) 
    case Operators.GreaterThanOrEquals ~ v => GreaterThanOrEquals(v) 
  }

  def comparisonExpression =  (opComparisonExpression | simpleComparisonExpression) ^^ { case e => e }


  def expression = (logicalExpression | inExpression | comparisonExpression) ^^ { case e => e }

  def expressionTail = "," ~ (comparisonExpression) ^^ { case _ ~ e => e }
  def expressionList = (comparisonExpression) ~ (expressionTail*) ^^ { case v ~ t => v :: t } 

  def comparator = (
    eq ^^^ Operators.Equals | 
    lt ^^^ Operators.LessThan | 
    le ^^^ Operators.LessThanOrEquals | 
    gt ^^^ Operators.GreaterThan | 
    ge ^^^ Operators.GreaterThanOrEquals | 
    ne ^^^ Operators.NotEquals
  )

  //def value = "[^&',]+"
  def integer = """[0-9]+""".r ^^ { case v => v.toLong}
  def double  = """[0-9]+.[0-9]+""".r ^^ { case v => v.toDouble}
  def boolean = "true|false".r ^^ { case v => v.toBoolean }
  def unquotedString = """[^,()]+""".r ^^ { case v => v }
  def unquotedValue = (integer | double | boolean | unquotedString)
  def quotedValue= """'[^&]+?'""".r ^^ { case v => v }
  def value = (quotedValue | unquotedValue) ^^ { case v => v }
  def valueListTail = ("," ~ value) ^^ { case _ ~ v => v }
  def valueList     = value ~ (valueListTail*) ^^ { case v ~ t => v :: t } 

  def parse[T](s: String): Condition[T] = {
    parseAll(expression, new CharSequenceReader(s)).get.asInstanceOf[Condition[T]]
  }
}
