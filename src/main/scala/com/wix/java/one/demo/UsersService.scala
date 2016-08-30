package com.wix.java.one.demo

import com.wix.java.one.demo.domain.User


class UsersService(dao: UsersDao) {
  def byId(id: String): Option[User] = dao.byId(id)
  def create(user: User): Unit = dao.insert(user).get
}
