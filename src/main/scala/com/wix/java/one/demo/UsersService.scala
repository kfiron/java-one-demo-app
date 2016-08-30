package com.wix.java.one.demo

import com.wix.java.one.demo.domain.User
import scala.util.Try


class UsersService(dao: UsersDao) {
  
  import UserValidator.validate

  def byId(id: String): Option[User] = dao.byId(id)

  def create(user: User): Try[Unit] = Try {
    validate(user)
    dao.insert(user)
  }
}

object UserValidator {
  def validate(user: User): Unit = {

    if(null == user.email)
      throw new IllegalArgumentException
    
    if(user.email == "")
      throw new IllegalArgumentException

    if (user.email.indexOf("@") == -1 )
      throw new IllegalArgumentException
    
    if(user.email.indexOf("@") == 0)
      throw new IllegalArgumentException
    
    if(user.email.indexOf("@") > user.email.indexOf("."))
      throw new IllegalArgumentException
    
    if(null == user.name)
      throw new IllegalArgumentException

    if("" == user.name)
      throw new IllegalArgumentException
  }
}
