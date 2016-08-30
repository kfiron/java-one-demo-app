package com.wix.java.one.demo.app

import com.wix.java.one.demo.JacksonSupport.asObject
import com.wix.java.one.demo.domain.User
import io.vertx.core.Handler
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpClientResponse
import org.specs2.matcher.Matcher
import org.specs2.matcher.Matchers._

import scala.concurrent.{Future, Promise}

trait UsersServerMatchers {

  def beCreated = be_==(201) ^^ { (_: HttpClientResponse).statusCode }
  def beNotFound = be_==(404) ^^ { (_: HttpClientResponse).statusCode }
  def beUserLike(u: User): Matcher[String] =
    be_===(u) ^^ { (r: String) => asObject(r) }

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
