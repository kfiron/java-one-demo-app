package com.wix.java.one.demo

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.{HttpEntity, HttpResponse}
import spray.json.DefaultJsonProtocol._
import com.wix.java.one.demo.domain.User
import spray.json.CompactPrinter
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._


object UsersRouter {


  implicit val userFormat = jsonFormat3(User)
  implicit val printer = CompactPrinter
  import JacksonSupport._

  val usersService = new UsersService(new InMemoryUsersDao)

  val router = get {
    path("users" / Segment) {
      id: String =>
        val response = usersService.byId(id: String).fold(HttpResponse(404))
                                        {u => HttpResponse(status = 200,
                                                            entity = HttpEntity(asJsonStr(u)))}
        complete(response)

    }
  } ~ post {
    path("users") {
      decodeRequest {
        entity(as[User]) {
          u =>
            usersService.create(u)
            complete(HttpResponse(status = 201))
        }
      }
    }
  }

}

