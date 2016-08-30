package com.wix.java.one.demo.app

import java.util.UUID

import com.wix.java.one.demo.UsersServerStarter
import com.wix.java.one.demo.app.JsonSupport.anyToJson
import com.wix.java.one.demo.domain.User
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.specification.Scope

class UsersServersITTest extends SpecificationWithJUnit
with UsersServerMatchers
with VertXClientBase
with UsersServerDriver {

  UsersServerStarter.start
  implicit val executionEnv = ExecutionEnv.fromGlobalExecutionContext

  trait UsersServerContext extends Scope {
    val userId = UUID.randomUUID.toString
    val user: User = User(userId, "kfkf@sss.com", "name")
  }

  "users server test" should {
    "load user" should {
      "does not exists" in new UsersServerContext {
        get(path = s"/users/$userId") must beNotFound.await
      }
      "for given user should return the user" in new UsersServerContext {
        givenUser(user)
        get(path = s"/users/$userId") must beUserLike(user).await
      }
    }
    "create user" should {
      "should be created" in new UsersServerContext {
        post(path = s"/users",
          data = user) must beCreated.await
      }
      "should return invalid request for bad input" in new UsersServerContext {
        post(path = s"/users",
          data = user.copy(email = "invalid-email")) must beBadRequest.await
      }
    }


  }


  def givenUser(user: User) = post(path = s"/users",
    data = user) must beCreated.await
}

