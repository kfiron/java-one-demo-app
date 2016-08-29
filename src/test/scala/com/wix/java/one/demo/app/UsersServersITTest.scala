package com.wix.java.one.demo.app

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.specification.Scope
import java.util.UUID
import com.wix.java.one.demo.UsersServerStarter
import JsonSupport.anyToJson
import com.wix.java.one.demo.domain.User

class UsersServersITTest extends SpecificationWithJUnit
with UsersServerMatchers {

  UsersServerStarter.start

  trait UsersServerContext extends Scope
  with VertXClientBase
  with UsersServerDriver {
    val userId = UUID.randomUUID.toString
    val user: User = User(userId, "kfkf@sss.com", "name")
  }

  "users server test" should {
    /*"load user by id return not exists" in new UsersServerContext {
      get(path = s"/users/$userId",
        assert = response => response must beNotFound)
    }
    "post user should be created" in new UsersServerContext {
      post(path = s"/users",
        data = user,
        assert = response => response must beCreated)
    }*/
    "post and load user" in new UsersServerContext {
      post(path = s"/users",
        data = user,
        assert = response => response must beCreated)
      get(path = s"/users/$userId",
        assert = response => response must beUserLike(user))

    }
  }
}

