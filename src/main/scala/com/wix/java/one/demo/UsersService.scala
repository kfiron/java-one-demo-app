package com.wix.java.one.demo

import com.wix.java.one.demo.domain.User
import scala.util.{Failure, Success, Try}
import com.workshop.ThrottlerFactory
import java.time.Clock
import scala.concurrent.duration._

class UsersService(dao: UsersDao, throttlerFactory: ThrottlerFactory) {

  import UserValidator.validate

  val throttler = throttlerFactory.throttlerFor(1, Clock.systemUTC(), 1.hour)

  def byId(id: String): Option[User] = dao.byId(id)
  def delete(id: String): Unit = dao.deleteBy(id).get
  def create(user: User, ip: String = "1.1.1.1"): Try[Unit] = Try {
    throttler.tryAcquire(ip) match {
      case Success(s) => validate(user)
                          dao.insert(user)
      case Failure(e) => throw e
    }

  }
}

object UserValidator {
  def validate(user: User): Unit = {

    if (null == user.email)
      throw new IllegalArgumentException

    if (user.email == "")
      throw new IllegalArgumentException

    if (user.email.indexOf("@") == -1)
      throw new IllegalArgumentException

    if (user.email.indexOf("@") == 0)
      throw new IllegalArgumentException

    if (user.email.indexOf("@") > user.email.indexOf("."))
      throw new IllegalArgumentException

    if (null == user.name)
      throw new IllegalArgumentException

    if ("" == user.name)
      throw new IllegalArgumentException
  }
}
