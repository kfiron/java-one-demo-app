package com.wix.java.one.demo

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.{HttpEntity, HttpResponse}
import spray.json.DefaultJsonProtocol._
import com.wix.java.one.demo.domain.{UsersList, User}
import spray.json.CompactPrinter
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import scala.util.{Failure, Success}


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
  } ~ delete {
    path("users" / Segment) {
      id: String =>      
        usersService.delete(id)
        complete(HttpResponse(status = 204))
    }
  } ~ post {
    path("users") {
      decodeRequest {
        entity(as[User]) {
          user =>
            usersService.create(user) match {
              case Success(u) => complete(HttpResponse(status = 201))
              case Failure(e) => complete(HttpResponse(status = 400))
            }
            
        }
      }
    }
  }

}

