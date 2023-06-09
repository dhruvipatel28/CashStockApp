package dhruvi.patel.cashstockapp.api

import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationInterceptor : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {

        //Take current request object copy and add headers to it
        val request = chain.request().newBuilder()

        request.addHeader("Authentication" ," My User Key")
        request.addHeader("LoginID" ," My User Key")

        return chain.proceed(request.build())

    }
}