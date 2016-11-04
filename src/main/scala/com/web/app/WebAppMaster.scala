package com.web.app

import akka.actor._
import akka.event.Logging
import com.web.messages.{StartWebWorkers, WebWorkersStarted,JsonEvent}

class WebAppMaster extends Actor {
  val log = Logging.getLogger( context.system, this )

  var apiServerMaster: ActorRef = _
  var webAppServerMaster: ActorRef = _
  var loadBalancer: ActorRef = _

  override def preStart(): Unit = {
    log.info( "[Info]-WebApplicationMaster Started ........" )
  }

  override def receive = {
    case w : JsonEvent => loadBalancer ! w
    case `StartWebWorkers` =>
      log.info( "[Info]-Starting APIServer........ " )
      val apiServerMaster = context.actorOf( Props( new APIServerMaster ), "api-server" )
      log.info( "[Info]-Starting WebApplicationServer........ " )
      val webAppServerMaster = context.actorOf( Props( new WebApplicationServerMaster(apiServerMaster) ), "web-app-server" )
      log.info( "[Info]-Starting LoadBalancer........ " )
      loadBalancer = context.actorOf( Props( new LoadBalancer( webAppServerMaster ) ), "load-balancer" )
      sender ! WebWorkersStarted
    case _ => log.warning( s"[Warning]-Unknown Event for ${self.path.name}" )
  }

}
