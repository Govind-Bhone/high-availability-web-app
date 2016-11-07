package com.web.app

import akka.actor._
import akka.event.Logging
import com.web.messages.{JsonEvent,StopActor}
import scala.util.parsing.json._
import akka.actor.PoisonPill

/**
  * Created by govind.bhone on 11/4/2016.
  */
class WebApplicationServerWorker(apiServerMaster: ActorRef) extends Actor {
  val log = Logging.getLogger( context.system, this )

  override def preStart(): Unit = {
    log.info( s"[Info]-WebApplicationServerWorker Started ${self.path} ........" )
  }

  override def postStop(): Unit ={
    log.warning(s"[Warning]-WebApplicationServerWorker Stopped ${self.path}........")
  }

  def remove(key: String)(x: Any): Any =
    x match {
      case m: Map[String, _] => m.mapValues( remove(key) ) - key
      case l: List[_] => l.map( remove(key) )
      case v => v
    }


  override def receive = {
    case w: JsonEvent =>
      val jsonObj = JSON.parseFull( w.json ).map( remove("hit_time") ).get.asInstanceOf[Map[String, Any]]
      apiServerMaster ! w.copy( json = scala.util.parsing.json.JSONObject( jsonObj ).toString( ) )
    case s @ StopActor =>
      self ! PoisonPill
      apiServerMaster ! s
    case _ => log.warning( s"[Warning]-Unknown Event to ${self.path}" )
  }

}