package com.tellius.connecionlevel

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.stream.ActorMaterializer
import com.tellius.Playground.UserDBActor

object ConnectionLevel extends App {

  implicit val system = ActorSystem("ConnectionLevel")
  implicit val materializer = ActorMaterializer()

  import system.dispatcher

  private val userDBActor: ActorRef = system.actorOf(Props[UserDBActor], "UserDBActor")

}
