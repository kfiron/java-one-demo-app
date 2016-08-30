package com.wix.java.one.demo.app

import java.util.UUID

import com.wix.java.one.demo.UsersServerStarter
import com.wix.java.one.demo.app.JsonSupport.anyToJson
import com.wix.java.one.demo.domain.{UsersList, User}
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.specification.Scope
import org.specs2.matcher.Matcher
import scala.concurrent.Future

class UsersServersITTest extends SpecificationWithJUnit
with UsersServerMatchers
with VertXClientBase
with UsersServerDriver {

  implicit def matcherToFutureMatcher[T](m: Matcher[T]): Matcher[Future[T]] = m.await
  
  sequential
  
  UsersServerStarter.start
  implicit val executionEnv = ExecutionEnv.fromGlobalExecutionContext

  trait UsersServerContext extends Scope {
    val userId = UUID.randomUUID.toString
    val user: User = User(userId, "kfkf@sss.com", "name")
  }

  "users server test" should {
    "load user" should {
      "does not exists" in new UsersServerContext {
        get(path = s"/users/$userId") must beNotFound
      }
      "for given user should return the user" in new UsersServerContext {
        givenUser(user)
        get(path = s"/users/$userId") must beUserLike(user)
      }
    }
    "create user" should {
      "should be created" in new UsersServerContext {
        post(path = s"/users",
          data = user) must beCreated
      }
      "should return invalid request for bad input" in new UsersServerContext {
        post(path = s"/users",
          data = user.copy(email = "invalid-email")) must beBadRequest
      }
    }
    "delete users" should {
      "should be deleted" in new UsersServerContext {
        givenUser(user)
        delete(path = s"/users/$userId") must beDeleted
        get(path = s"/users/$userId") must beNotFound
      }      
    }


  }


  def givenUser(user: User) = post(path = s"/users",
    data = user) must beCreated.await
}

