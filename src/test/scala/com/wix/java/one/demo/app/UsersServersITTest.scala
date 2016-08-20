package com.wix.java.one.demo.app

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.specification.Scope
import java.util.UUID
import com.wix.java.one.demo.UsersServerStarter


class UsersServersITTest extends SpecificationWithJUnit
with UsersServerMatchers {


  trait UsersServerContext extends Scope
  with VertXClientBase
  with UsersServerDriver {
    UsersServerStarter.start
    val userId = UUID.randomUUID.toString
  }


  "users server test" should {
    "load user by id return not exists" in new UsersServerContext {
      get(s"/users/$userId", response => response must beNotFound)
    }
  }
}



