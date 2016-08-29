package com.wix.java.one.demo

import scala.util.Try
import com.wix.java.one.demo.domain.User


trait UsersDao {
  def byId(id: String): Option[User]
  def insert(user: User): Try[Unit]
  def deleteBy(id: String): Try[Unit]
}

class InMemoryUsersDao extends UsersDao{
  
  val users = scala.collection.mutable.HashMap.empty[String, User]
  
  override def byId(id: String): Option[User] = users.get(id)
  override def insert(user: User): Try[Unit] = Try(users += user.id -> user)
  override def deleteBy(id: String): Try[Unit] = Try(users -= id)
}
