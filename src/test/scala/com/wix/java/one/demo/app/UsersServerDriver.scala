package com.wix.java.one.demo.app

import io.vertx.core.http.HttpClientResponse
import scala.concurrent.{Await, Promise}
import io.vertx.core.{Vertx, Handler}
import scala.concurrent.duration._
import com.wix.java.one.demo.UsersServerStarter

trait TransportBase {
  
  def handler[T](p: Promise[T], assert: HttpClientResponse => T) = new Handler[HttpClientResponse] {
    override def handle(response: HttpClientResponse): Unit = {
      p success assert(response)
    }
  }

  def execute[T](f: Promise[T] => Any) = {
    val p = Promise[T]()
    f(p)
    Await.result(p.future, 3.seconds)
  }

}

trait UsersServerDriver extends TransportBase {
  self: VertXClientBase =>
  def get[T](path: String, assert: HttpClientResponse => T) = {
    execute{ p: Promise[T] =>
      httpClient.getNow(UsersServerStarter.port, "localhost", path, handler(p, assert))
    }
  }
  
  def post[T](path: String, data: String, assert: HttpClientResponse => T) = {
    execute { p: Promise[T] =>
      httpClient.post(UsersServerStarter.port, "localhost", path, handler(p, assert))
                .end(data)
    }    
  }
}

trait VertXClientBase {
  val vertx = Vertx.vertx
  val httpClient = vertx.createHttpClient
}
