package com.tellius.connecionlevel


import akka.actor.{ActorRef, ActorSystem, Props}


import akka.stream.ActorMaterializer


import com.tellius.Playground.UserDBActor



object ConnectionLevel extends App {
  println("ConnectionLevel")
  println("ConnectionLevel")
  println("ConnectionLevel")
  println("ConnectionLevel")
  println("ConnectionLevel")
  println("ConnectionLevel")
  println("ConnectionLevel")

  println("ConnectionLevel")
  println("ConnectionLevel")
  println("ConnectionLevel")
  println("ConnectionLevel")
  println("ConnectionLevel")
  println("ConnectionLevel")

  implicit val system = ActorSystem("ConnectionLevel")
  println("ConnectionLevel")
  println("ConnectionLevel")
  println("ConnectionLevel")
  println("ConnectionLevel")
  println("ConnectionLevel")
  println("ConnectionLevel")
  println("ConnectionLevel")

  println("ConnectionLevel")
  println("ConnectionLevel")
  println("ConnectionLevel")
  println("ConnectionLevel")
  println("ConnectionLevel")
  println("ConnectionLevel")
  implicit val materializer = ActorMaterializer()
  println("ConnectionLevel")
  println("ConnectionLevel")
  println("ConnectionLevel")
  println("ConnectionLevel")
  println("ConnectionLevel")
  println("ConnectionLevel")


  import system.dispatcher

  println("ConnectionLevel")
  println("ConnectionLevel")
  println("ConnectionLevel")
  println("ConnectionLevel")
  println("ConnectionLevel")
  println("ConnectionLevel")

  private val userDBActor: ActorRef = system.actorOf(Props[UserDBActor], "UserDBActor")



}
