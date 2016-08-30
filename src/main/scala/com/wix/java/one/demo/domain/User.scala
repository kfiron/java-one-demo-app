package com.wix.java.one.demo.domain

case class User(id: String, email: String, name: String)
case class UsersList(users: Seq[User] = Seq.empty[User])