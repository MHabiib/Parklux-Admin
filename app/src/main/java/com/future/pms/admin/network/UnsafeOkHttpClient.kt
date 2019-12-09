package com.future.pms.admin.network

import okhttp3.OkHttpClient
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object UnsafeOkHttpClient {
  val unsafeOkHttpClient: OkHttpClient
    get() {
      try {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
          override fun checkClientTrusted(
            chain: Array<java.security.cert.X509Certificate>, authType: String
          ) { //No Implement Required
          }

          override fun checkServerTrusted(
            chain: Array<java.security.cert.X509Certificate>, authType: String
          ) {  //No Implement Required
          }

          override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
            return arrayOf()
          }
        })
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        val sslSocketFactory = sslContext.socketFactory

        val builder = OkHttpClient.Builder()
        builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        builder.hostnameVerifier { hostname, session -> true }
        return builder.build()
      } catch (e: Exception) {
        throw RuntimeException(e)
      }
    }
}