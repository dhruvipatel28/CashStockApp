package dhruvi.patel.cashstockapp.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object RetrofitHelper {

    private const val BASE_URL = "https://storage.googleapis.com/cash-homework/cash-stocks-api/"

    var gson = GsonBuilder()
        .setLenient()
        .create()

    fun getInstance() : Retrofit {

        return  Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    fun provideOkHttpClient(authenticationInterceptor: AuthenticationInterceptor) : OkHttpClient{
        return OkHttpClient.Builder().addInterceptor(authenticationInterceptor).build()
    }

}


