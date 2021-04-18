package com.myapplication.retrofit

import com.myapplication.utils.AppConstant.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitRequest {

    companion object {
        private var retrofit: Retrofit? = null

        fun getRetrofitInstance(): Retrofit? {
            if (retrofit == null) {

                val httpClient = OkHttpClient.Builder()
                    .addInterceptor(QueryParameterAddInterceptor())

                val client = httpClient.build()

                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()

            }
            return retrofit
        }
    }
}