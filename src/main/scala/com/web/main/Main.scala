package com.web.main

/**
  * Created by govind.bhone on 11/3/2016.
  */

import java.util.concurrent.TimeUnit

import akka.actor.{Props, ActorSystem}
import com.web.app.WebAppMaster
import com.web.messages.{Stop, StartWebWorkers, JsonEvent}
import scala.concurrent.AwaitAPIServerMaster
import scala.concurrent.duration._
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App {
  implicit val timeout = Timeout( 10, TimeUnit.SECONDS )
  val actorSystem = ActorSystem( "web-app" )

  val apiServer = actorSystem.actorOf( Props( new WebAppMaster ), "web-app-master" )
  Await.result( apiServer ? StartWebWorkers, Duration( 5, TimeUnit.SECONDS ) )

  val dataEvent = JsonEvent(
    s"""{
                 "device_type":"Desktop",
                 "browser_string":"Chrome 47.0.2526",
                 "ip":"62.82.34.0",
                 "os":"Windows 7",
                 "browser_version":"47.0.2526",
                 "session":"10",
                 "country_code":"IN",
                 "document_encoding":"UTF-8",
                 "city":"Pune",
                 "tz":"IST",
                 "uuid":"123323323",
                 "ts":"456789087878787",
                 "os_version":"10.11.2",
                 "hit_time":"2016-01-05T17:37:08",
                 "user_language":"es",
                 "account":196,
                 "url":"http://someurl.com",
                 "action":"click",
                 "country":"India",
                 "region":"Islas Baleares",
                 "referrer":"www.google.com",
                 "browser":"chrome"
                }""".stripMargin )

  actorSystem.scheduler.schedule( 5.seconds, 1.seconds ) {
    apiServer ! dataEvent
  }

  actorSystem.scheduler.schedule(5.seconds,60.seconds){
    apiServer ! Stop
  }

}
