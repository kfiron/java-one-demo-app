package com.wix.java.one.demo.app

import io.vertx.core.http.HttpClientResponse
import scala.concurrent.{Await, Promise}
import io.vertx.core.{Vertx, Handler}
import scala.concurrent.duration._
import com.wix.java.one.demo.UsersServerStarter

trait UsersServerDriver {
  self: VertXClientBase =>
  def get[T](path: String, f: HttpClientResponse => T) = {
    
    val p = Promise[T]()
    
    httpClient.getNow(UsersServerStarter.port, "localhost", path, new Handler[HttpClientResponse] {
      override def handle(response: HttpClientResponse): Unit = {
        p success f(response)
      }
    })

    Await.result(p.future, 3.seconds)
  }

}

trait VertXClientBase {
  val vertx = Vertx.vertx
  val httpClient = vertx.createHttpClient
}
