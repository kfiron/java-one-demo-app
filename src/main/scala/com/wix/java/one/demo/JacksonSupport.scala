package com.wix.java.one.demo

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.wix.java.one.demo.domain.User


object JacksonSupport {

  val mapper = new ObjectMapper
  mapper.registerModule(new DefaultScalaModule)
  
  def asJsonStr(o: Any): String = mapper.writeValueAsString(o)
  def asObject(s: String): User = mapper.readValue(s, classOf[User])

}
