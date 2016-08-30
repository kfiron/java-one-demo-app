package com.wix.java.one.demo

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.wix.java.one.demo.domain.{UsersList, User}


object JacksonSupport {

  val mapper = new ObjectMapper
  mapper.registerModule(new DefaultScalaModule)
  
  def asJsonStr(o: Any): String = mapper.writeValueAsString(o)
  def asObject(s: String): User = mapper.readValue(s, classOf[User])
  def asUsers(s: String): UsersList = {
    println("kfiron")
    println(s)
    mapper.readValue(s, classOf[UsersList])
  }

}
