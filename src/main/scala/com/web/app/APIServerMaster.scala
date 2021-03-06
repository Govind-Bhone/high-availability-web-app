package com.web.app

import akka.actor.{Props, Actor}
import akka.event.Logging
import akka.routing.{Router, ActorRefRoutee,SmallestMailboxRoutingLogic}
import com.web.messages.{JsonEvent,StopActor}
import akka.actor.Terminated
/**
  * Created by govind.bhone on 11/3/2016.
  */
class APIServerMaster extends Actor{
  val log = Logging.getLogger(context.system, this)

  var apiServerRouter = {
    val routees = Vector.fill(3) {
      val r = context.actorOf(Props[APIServerWorker])
      context watch r
      ActorRefRoutee(r)
    }
    Router(SmallestMailboxRoutingLogic(), routees)
  }

  override def preStart(): Unit ={
    log.info("[Info]-APIServerMaster Started........")
  }

  override def postStop(): Unit ={
    log.warning("[Warning]-APIServerMaster Stopped........")
  }


  override def receive ={
    case w : JsonEvent =>
      apiServerRouter.route(w, sender())
    case s @ StopActor => apiServerRouter.route(s, sender())
    case Terminated(a) =>
      log.warning(s"[Warning]-Routee ${a.path} is Terminated")
      apiServerRouter = apiServerRouter.removeRoutee(a)
      val r = context.actorOf(Props[APIServerWorker])
      context watch r
      log.info(s"[Info]-Routee ${a.path} is Added to Routee List")
      apiServerRouter = apiServerRouter.addRoutee(r)
    case _ => log.warning(s"[Warning]-Unknown Event to ${self.path}")
  }

}
