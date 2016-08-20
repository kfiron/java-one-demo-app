package com.wix.java.one.demo

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.HttpResponse

object UsersRouter {


  val router = get {
    path("users" / Segment) { id: String =>
      complete(HttpResponse(404))
    }
  }
}
