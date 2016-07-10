package o3co.store.entity

/**
 *
 *
 * {{{
 *   object base extends ServiceDefinition[ID, Entity]
 *
 *   trait Service extends base.Service {
 *     // any extension here
 *   }
 *
 *   object Service extends base.ServiceFactory {
 *     // 
 *   }
 *
 *   class ServiceAdapter extends base.ServiceAdapter {
 *      val protocol = Protocol
 *   }
 * }}}
 */
trait BaseServiceDefinition[I, E <: Entity[I]] {
  
  trait Protocol extends EntityStoreProtocol[I, E]

  trait Service extends EntityStore[I, E]

  trait ServiceAdapter extends ActorSelectionEntityStore[I, E] {
    val protocol: Protocol
  }

  trait ServiceActor extends EntityStoreActor[I, E] {
    this: Service => 

    val protocol: Protocol
  }

  trait ServiceFactory extends o3co.ServiceFactory[Service]

  trait ServiceAdapterFactory extends o3co.ServiceAdapterFactory[Service]
}
