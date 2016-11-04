package com.web.app

import akka.actor._
import akka.event.Logging
import com.web.messages.JsonEvent

/**
  * Created by govind.bhone on 11/4/2016.
  */
class WebApplicationServerWorker(apiServerMaster:ActorRef) extends Actor{
  val log = Logging.getLogger(context.system, this)

  override def preStart(): Unit ={
    log.info(s"[Info]-WebApplicationServerWorker Started ${self.path.name} ........")
  }

  override def receive ={
    case w : JsonEvent =>apiServerMaster ! w
    case _ => log.warning(s"[Warning]-Unknown Event to ${self.path.name}")
  }

}