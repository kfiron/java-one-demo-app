package com.wix.java.one.demo

import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.actor.ActorSystem
import UsersRouter.router

object UsersServerStarter {

  val port = 8877
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  def start = Http().bindAndHandle(router, "localhost", port)
}


object Main extends App {
  UsersServerStarter.start
}