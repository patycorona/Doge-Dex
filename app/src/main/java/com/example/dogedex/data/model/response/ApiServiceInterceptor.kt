package com.example.dogedex.data.model.response

import com.example.dogedex.domain.model.ConstantGeneral.Companion.NEEDS_AUTH_HEADER_KAY
import okhttp3.Interceptor
import okhttp3.Response
import java.lang.RuntimeException

object ApiServiceInterceptor: Interceptor {

    private var sessionToken:String? = null

    fun setSessionToken(sessionToken:String){
        this.sessionToken = sessionToken

    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()
        if (request.header(NEEDS_AUTH_HEADER_KAY) != null){
            //need credencial
            if (sessionToken  == null){
                throw RuntimeException("Need  to be authentication to perform")
            }
            else{
                requestBuilder.addHeader("AUTH-TOKEN", sessionToken!!)
            }
        }
        return chain.proceed(requestBuilder.build())
    }
}