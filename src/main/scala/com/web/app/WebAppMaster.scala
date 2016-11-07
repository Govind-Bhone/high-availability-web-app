package com.web.app

import akka.actor._
import akka.event.Logging
import com.web.messages.{StartWebWorkers, WebWorkersStarted, JsonEvent}
import akka.actor.OneForOneStrategy
import akka.actor.SupervisorStrategy._

class WebAppMaster extends Actor {
  val log = Logging.getLogger( context.system, this )

  var apiServerMaster: ActorRef = _
  var webAppServerMaster: ActorRef = _
  var loadBalancer: ActorRef = _

  override val supervisorStrategy =
    OneForOneStrategy( maxNrOfRetries = 10, withinTimeRange = 1 minute ) {
      case _: ArithmeticException => Resume
      case _: NullPointerException => Restart
      case _: IllegalArgumentException => Stop
      case _: Exception => Escalate
    }

  override def preStart(): Unit = {
    log.info( "[Info]-WebApplicationMaster Started ........" )
  }

  override def postStop(): Unit ={
    log.warning("[Warning]-WebApplicationMaster Stopped........")
  }

  override def receive = {
    case w: JsonEvent => loadBalancer ! w
    case `StartWebWorkers` =>
      log.info( "[Info]-Starting APIServer........ " )
      val apiServerMaster = context.actorOf( Props( new APIServerMaster ), "api-server" )
      context.watch( apiServerMaster )
      log.info( "[Info]-Starting WebApplicationServer........ " )
      val webAppServerMaster = context.actorOf( Props( new WebApplicationServerMaster( apiServerMaster ) ), "web-app-server" )
      context.watch( webAppServerMaster )
      log.info( "[Info]-Starting LoadBalancer........ " )
      loadBalancer = context.actorOf( Props( new LoadBalancer( webAppServerMaster ) ), "load-balancer" )
      context.watch( loadBalancer )
      sender ! WebWorkersStarted
    case s@Stop => loadBalancer ! s
    case _ => log.warning( s"[Warning]-Unknown Event for ${self.path.name}" )
  }

}
