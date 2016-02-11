package jp.o3co.search

sealed trait OrdinalDirection
object OrdinalDirections {
  case object ASC extends OrdinalDirection
  case object DESC extends OrdinalDirection
}

