package o3co.text

type DomainText = (Option[Domain], String)

trait TextStoreService[ID] extends KeyValueStore[ID, (Option[Domain], String)] {
}

trait TextStoreServiceProtocol[ID] extends KeyValueStoreProtocol[ID, (Option[Domain], String)]

trait TextStoreServiceActorLike[ID] extends KeyValueStoreActorLike[ID, (Option[Domain], String)] {
  this: Actor with TextStoreService[ID] =>

  def receiveTextStoreCommands = receiveKeyValueStoreCommands
}

trait TextStoreServiceActor[ID] extends ServiceActor with TextStoreServiceActorLike[ID] {
  this: TextStoreService[ID] => 

  def receive = receiveTextStoreCommands orElse receiveExtension
}


trait {
  def idProvider: IDProviderService[ID]

  def createAsync(text: String, domain: Option[Domain] = None) = {
    idProvider.generateAsync()
      .flatMap { id => 
        putAsync(id, (domain, text))
          .map(_ => id)
          .recover {
            case e: Throwable => 
              idProvider.releaseAsync(id)
              throw e
          }
      }
  }

  def deleteByDomainAsync(domain: Domain) = {
    findByDomainAsync(domain).flatMap { ids => 
      deleteAync(ids)
    }
  }
}
