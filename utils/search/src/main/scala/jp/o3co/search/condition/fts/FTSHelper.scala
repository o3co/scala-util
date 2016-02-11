package jp.o3co.search.condition.fts

/**
 * Helper to create Condition for FullTextSearch
 */
trait FTSHelper {

  /**
   *
   */
  def term(value: String)           = ContainsTerm(value)   // Term or Phrase
}
