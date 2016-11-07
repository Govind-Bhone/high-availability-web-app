package com.web.app

import akka.actor._
import akka.event.Logging
import akka.routing.{Router,SmallestMailboxRoutingLogic}
import akka.routing.ActorRefRoutee
import com.web.messages.{JsonEvent,StopActor}
import akka.actor.Terminated

/**
  * Created by govind.bhone on 11/3/2016.
  */
class WebApplicationServerMaster(apiServerMaster:ActorRef) extends Actor{
  val log = Logging.getLogger(context.system, this)

  var webAppRouter = {
    val routees = Vector.fill(100) {
      val r = context.actorOf(Props(new WebApplicationServerWorker(apiServerMaster)))
      context watch r
      ActorRefRoutee(r)
    }
    Router(SmallestMailboxRoutingLogic(), routees)
  }

  override def preStart(): Unit ={
    log.info("[Info]-WebApplicationServerMaster Started")
  }

  override def postStop(): Unit ={
    log.warning("[Warning]-WebApplicationServerMaster Stopped........")
  }

  override def receive ={
    case w : JsonEvent =>
      webAppRouter.route(w, sender())
    case s @ StopActor => webAppRouter.route(s, sender())
    case Terminated(a) =>
      log.warning(s"[Warning]-Routee ${a.path} is Terminated")
      webAppRouter = webAppRouter.removeRoutee(a)
      val r = context.actorOf(Props(new WebApplicationServerWorker(apiServerMaster)))
      context watch r
      log.info(s"[Info]-Routee ${a.path} is Added to Routee List")
      webAppRouter = webAppRouter.addRoutee(r)
    case _ => log.warning(s"[Warning]-Unknown Event to ${self.path}")
  }

}
