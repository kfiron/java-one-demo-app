package com.wix.java.one.demo.app

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.specification.Scope
import java.util.UUID


class UsersServersITTest extends SpecificationWithJUnit
with UsersServerMatchers {


  trait UsersServerContext extends Scope
  with VertXClientBase
  with UsersServerDriver {
    
    val userId = UUID.randomUUID
  }


  "users server test" should {
    "load user by id return not exists" in new UsersServerContext {
      get(s"/users/$userId", response => response must beNotFound)
    }
  }
}



