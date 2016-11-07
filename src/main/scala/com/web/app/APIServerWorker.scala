package com.web.app

import akka.actor.{Props, Actor}
import akka.event.Logging
import com.web.messages.{JsonEvent,StopActor}
import akka.actor.PoisonPill

/**
  * Created by govind.bhone on 11/4/2016.
  */

class APIServerWorker extends Actor{
  val log = Logging.getLogger(context.system, this)

  override def preStart(): Unit ={
    log.info(s"[Info]-APIServerWorker Started ${self.path}........")
  }

  override def postStop(): Unit ={
    log.warning(s"[Warning]-APIServerWorker Stopped ${self.path}........")
  }

  override def receive ={
    case w : JsonEvent =>log.info(s"Processed Json is ${w.json}")
    case s @ StopActor => self ! PoisonPill
    case _ => log.warning(s"[Warning]-Unknown Event to ${self.path}")
  }

}

