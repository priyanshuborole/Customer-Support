package com.priyanshub.branchchat.di

import android.content.Context
import com.priyanshub.branchchat.common.Constants
import com.priyanshub.branchchat.data.remote.CustomerServiceRequest
import com.priyanshub.branchchat.data.repository.CustomerServiceRepositoryImpl
import com.priyanshub.branchchat.domain.repository.CustomerServiceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNewsRequestApi(
        @ApplicationContext context: Context
    ): CustomerServiceRequest {
        val client = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.MINUTES)
            .connectionPool(ConnectionPool(5, 5, TimeUnit.MINUTES))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(CustomerServiceRequest::class.java)
    }

    @Provides
    @Singleton
    fun provideCustomerServiceRepository(api: CustomerServiceRequest): CustomerServiceRepository {
        return CustomerServiceRepositoryImpl(api)
    }


}