package com.wix.java.one.demo.app

import java.util.UUID

import com.wix.java.one.demo.UsersServerStarter
import com.wix.java.one.demo.app.JsonSupport.anyToJson
import com.wix.java.one.demo.domain.User
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.specification.Scope

class UsersServersITTest extends SpecificationWithJUnit with UsersServerMatchers {

  UsersServerStarter.start
  implicit val executionEnv = ExecutionEnv.fromGlobalExecutionContext

  trait UsersServerContext extends Scope
  with VertXClientBase
  with UsersServerDriver {
    val userId = UUID.randomUUID.toString
    val user: User = User(userId, "kfkf@sss.com", "name")
  }

  "users server test" should {
    "load user by id return not exists" in new UsersServerContext {
      get(path = s"/users/$userId") must beNotFound.await
    }
    "post user should be created" in new UsersServerContext {
      post(path = s"/users",
        data = user) must beCreated.await
    }
    "post and load user" in new UsersServerContext {
      post(path = s"/users",
        data = user) must beCreated.await
      get(path = s"/users/$userId") must beUserLike(user).await

    }
  }
}

