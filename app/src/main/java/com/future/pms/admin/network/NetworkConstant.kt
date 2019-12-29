package com.future.pms.admin.network

object NetworkConstant {
  const val BASE = "http://192.168.18.207:8088/"
  //  const val BASE = "http://10.0.2.2:8088/" //on emulator
  const val USERNAME = "pms-client"
  const val PASSWORD = "pms-secret"
  const val GRANT_TYPE = "password"
  const val AUTHORIZATION = "Authorization"
  const val WRITE_TIMEOUT = 30L
  const val READ_TIMEOUT = 30L
}