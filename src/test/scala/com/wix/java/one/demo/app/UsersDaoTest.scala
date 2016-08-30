package com.wix.java.one.demo.app

import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.Scope
import com.wix.java.one.demo.{InMemoryUsersDao, UsersDao}
import java.util.UUID
import com.wix.java.one.demo.domain.User


class UsersDaoTest extends SpecWithJUnit {

  trait UsersDaoCtx extends Scope {
    val dao: UsersDao = new InMemoryUsersDao
    val userId = UUID.randomUUID.toString
    val user = User(id = userId,
                    email = "dan@gmail.com",
                    name = "dan") 
  }

  "users dao" should {
    "load users not exists" in new UsersDaoCtx {
      dao.byId(userId) must beNone
    }
    "insert and get user" in new UsersDaoCtx {
      dao.insert(user) must beSuccessfulTry
      dao.byId(userId) must beSome(user)
    }
    "delete user" in new UsersDaoCtx {
      dao.insert(user) must beSuccessfulTry
      dao.deleteBy(id = user.id) must beSuccessfulTry
      dao.byId(userId) must beNone
    }
  }

}
