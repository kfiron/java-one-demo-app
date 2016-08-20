package com.wix.java.one.demo

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._


object UsersRouter {


  final case class User(id: String, email: String, name: String)

  implicit val userFormat = jsonFormat3(User)

  val router = get {
    path("users" / Segment) {
      id: String =>
        complete(HttpResponse(404))
    }
  } ~ post {
    path("users") {
      decodeRequest {
        entity(as[User]) {
          s =>
            complete(HttpResponse(201))
        }
      }
    }
  }

}

