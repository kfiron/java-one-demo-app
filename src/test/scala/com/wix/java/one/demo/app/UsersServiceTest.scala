package com.wix.java.one.demo.app

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.specification.Scope
import org.specs2.mock.Mockito
import com.wix.java.one.demo.{UsersDao, UsersService}
import java.util.UUID
import com.wix.java.one.demo.domain.User
import scala.util.Try


class UsersServiceTest extends SpecificationWithJUnit with Mockito {

  trait UsersServiceScope extends Scope {
    val dao = mock[UsersDao]
    val id = UUID.randomUUID.toString
    val user = User(id = id,
      email = "kfirb@wix.com",
      name = "kfir bloch")
    val usersService = new UsersService(dao)

    def givenUser(user: User): User = {
      dao.insert(user) returns Try(Unit)
      user
    }
    
    def create(user: User) = usersService.create(user)

  }

  "users service test" should {
    "validate user" should {
      "validate name" should {
        "return valid" in new UsersServiceScope {
          val validNameUser = givenUser(user.copy(name = "kfir bloch"))
          create(validNameUser) must beSuccessfulTry
        }
        "return illegal because null" in new UsersServiceScope {
          val inValidNameUser = givenUser(user.copy(name = null))
          create(inValidNameUser) must beFailedTry.withThrowable[IllegalArgumentException]
        }
        "return illegal because empty" in new UsersServiceScope {
          val inValidNameUser = givenUser(user.copy(name = ""))
          create(inValidNameUser) must beFailedTry.withThrowable[IllegalArgumentException]
        }
      }
      "validate email" should {
        "return valid" in new UsersServiceScope {
          val validEmailUser = givenUser(user.copy("kfirb@wix.com"))
          create(validEmailUser) must beSuccessfulTry
        }
        "return illegal argument because empty" in new UsersServiceScope {
          val invalidEmailUser = givenUser(user.copy(email = ""))
          create(invalidEmailUser) must beFailedTry.withThrowable[IllegalArgumentException]
        }
        "return illegal argument because null" in new UsersServiceScope {
          val invalidEmailUser = givenUser(user.copy(email = null))
          create(invalidEmailUser) must beFailedTry.withThrowable[IllegalArgumentException]
        }
        "return illegal argument because without @" in new UsersServiceScope {
          val invalidEmailUser = givenUser(user.copy(email = "invalidEmail"))
          create(invalidEmailUser) must beFailedTry.withThrowable[IllegalArgumentException]
        }
        "return illegal argument because no chars before @" in new UsersServiceScope {
          val invalidEmailUser = givenUser(user.copy(email = "@invalid"))
          create(invalidEmailUser) must beFailedTry.withThrowable[IllegalArgumentException]
        }
        "return illegal argument because . after the @" in new UsersServiceScope {
          val invalidEmailUser = givenUser(user.copy(email = "invalid@invalid"))
          create(invalidEmailUser) must beFailedTry.withThrowable[IllegalArgumentException]
        }
      }

    }
  }

}
