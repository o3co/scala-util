package o3co.search

import o3co.domain.DomainName

object DomainNameCondition extends ConditionFactory {
  
  def eqOrPrefix(domain: DomainName) = or(eq(domain), prefix[DomainName](domain.name + "."))
}
