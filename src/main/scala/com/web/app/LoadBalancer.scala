package com.web.app

import akka.actor.{Props, Actor}
import akka.actor._
import akka.event.Logging
import akka.routing.{Router, ActorRefRoutee}
import com.web.messages.{JsonEvent,StopActor}

/**
  * Created by govind.bhone on 11/4/2016.
  */
class LoadBalancer(webAppServerMaster: ActorRef) extends Actor {
  val log = Logging.getLogger( context.system, this )

  override def preStart(): Unit = {
    log.info( "[Info]-LoadBalancer Started........" )
  }

  override def postStop(): Unit ={
    log.warning("[Warning]-LoadBalancer Stopped........")
  }

  override def receive = {
    case w : JsonEvent => webAppServerMaster ! w
    case s @ StopActor => webAppServerMaster ! s
    case _ => log.warning( s"[Warning]-Unknown Event to ${self.path}" )
  }

}
