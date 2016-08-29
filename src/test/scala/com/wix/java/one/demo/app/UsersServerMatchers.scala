package com.wix.java.one.demo.app

import io.vertx.core.http.HttpClientResponse
import org.specs2.matcher.Matchers
import io.vertx.core.Handler
import io.vertx.core.buffer.Buffer
import scala.concurrent.{Await, Promise}
import scala.concurrent.duration._
import com.wix.java.one.demo.domain.User
import com.wix.java.one.demo.JacksonSupport.asObject

trait UsersServerMatchers {
  self: Matchers =>
  
  def beCreated = be_==(201) ^^ {
    (t: HttpClientResponse) => t.statusCode
  }

  def beNotFound = be_==(404) ^^ {
    (t: HttpClientResponse) => t.statusCode
  }
  
  def beUserLike(u: User) = be_==(u) ^^ {
    (t: HttpClientResponse) => {
      val p = Promise[User]()
      t.bodyHandler(new Handler[Buffer] {
        override def handle(buffer: Buffer): Unit = {
          p success asObject(new String(buffer.getBytes))
        }
      })
      Await.result(p.future, 6.seconds)
    }
  }

}
