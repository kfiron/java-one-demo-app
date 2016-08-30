package com.wix.java.one.demo.app

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.wix.java.one.demo.UsersServerStarter
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpClientResponse
import io.vertx.core.{Handler, Vertx}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future, Promise}

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

case class Response(body: String, statusCode: Int)

trait UsersServerDriver extends TransportBase { self: VertXClientBase =>

  def get[T](path: String): Future[HttpClientResponse] = {
    val p = Promise[HttpClientResponse]()
    httpClient.getNow(UsersServerStarter.port, "localhost", path, new Handler[HttpClientResponse] {
      def handle(event: HttpClientResponse) = p.success(event)
    })
    p.future
  }

  def post[T](path: String, data: String): Future[HttpClientResponse] = {
    val p = Promise[HttpClientResponse]()
    httpClient.post(UsersServerStarter.port, "localhost", path, new Handler[HttpClientResponse] {
      def handle(event: HttpClientResponse) = p.success(event)
    })
      .putHeader("Content-Type", "application/json")
      .end(data)
    p.future

  }


  private implicit class HttpClientResponseParser(r: HttpClientResponse) {
    def responseBody: Future[String] = {
      val p = Promise[String]()
      r.handler(new Handler[Buffer] {
        def handle(buffer: Buffer): Unit = {
          p success new String(buffer.getBytes, "UTF-8")
        }

      })
      r.exceptionHandler(new Handler[Throwable] {
        def handle(event: Throwable) = p failure event
      })
      p.future
    }
  }
}

trait VertXClientBase {
  val vertx = Vertx.vertx
  val httpClient = vertx.createHttpClient
}

object JsonSupport {
  val m = new ObjectMapper()
  m.registerModule(new DefaultScalaModule)
  implicit def anyToJson(o: AnyRef): String = m.writeValueAsString(o)
}
