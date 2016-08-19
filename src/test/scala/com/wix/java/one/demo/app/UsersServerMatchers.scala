package com.wix.java.one.demo.app

import io.vertx.core.http.HttpClientResponse
import org.specs2.matcher.Matchers

trait UsersServerMatchers {
  self: Matchers =>
  
  def beCreated = be_==(201) ^^ {
    (t: HttpClientResponse) => t.statusCode
  }

  def beNotFound = be_==(404) ^^ {
    (t: HttpClientResponse) => t.statusCode
  }

}
