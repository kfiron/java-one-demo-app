package com.wix.java.one.demo.app

import com.wix.java.one.demo.JacksonSupport._
import com.wix.java.one.demo.domain.{UsersList, User}
import io.vertx.core.Handler
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpClientResponse
import org.specs2.matcher.{Matchers, Matcher}
import Matchers._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.{Future, Promise}
import org.specs2.concurrent.ExecutionEnv


trait UsersServerMatchers {

  def beCreated = be_==(201) ^^ {
    (_: HttpClientResponse).statusCode
  }

  def beNotFound = be_==(404) ^^ {
    (_: HttpClientResponse).statusCode
  }

  def beBadRequest = be_==(400) ^^ {
    (_: HttpClientResponse).statusCode
  }

  def beDeleted = be_==(204) ^^ {
    (_: HttpClientResponse).statusCode
  }

  def beTooManyRequests = be_==(429) ^^ {
    (_: HttpClientResponse).statusCode
  }
  
  def beUserLike(u: User)(implicit env: ExecutionEnv): Matcher[HttpClientResponse] =
    be_===(u).await ^^ {
      (r: HttpClientResponse) =>
        val p = Promise[String]()
        r.bodyHandler(new Handler[Buffer] {
          def handle(event: Buffer): Unit =
            p.success(new String(event.getBytes, "UTF-8"))
        })
        p.future.map(asObject)
    }

  implicit class HttpClientResponseParser(r: HttpClientResponse) {
    def asString: Future[String] = {
      val p = Promise[String]()
      r.handler(new Handler[Buffer] {
        def handle(buffer: Buffer): Unit = {
          println(new String(buffer.getBytes))
          p success new String(buffer.getBytes)
        }

      })
      r.exceptionHandler(new Handler[Throwable] {
        def handle(event: Throwable) = event.printStackTrace()
      })
      p.future
    }
  }

}
