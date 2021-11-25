package com.vmakd1916gmail.com.login_logout_register.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.vmakd1916gmail.com.login_logout_register.DB.MySocialNetworkDAO
import com.vmakd1916gmail.com.login_logout_register.DB.MySocialNetworkDatabase
import com.vmakd1916gmail.com.login_logout_register.R
import com.vmakd1916gmail.com.login_logout_register.repositories.auth.AuthRepositoryImpl
import com.vmakd1916gmail.com.login_logout_register.repositories.auth.RequestTokenInterceptor
import com.vmakd1916gmail.com.login_logout_register.repositories.data.DataRepositoryImpl
import com.vmakd1916gmail.com.login_logout_register.repositories.verify.VerifyRepositoryImpl
import com.vmakd1916gmail.com.login_logout_register.services.AuthService
import com.vmakd1916gmail.com.login_logout_register.services.DataService
import com.vmakd1916gmail.com.login_logout_register.services.VerifyService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val DATABASE_NAME = "social_network_database"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplicationContext(
        @ApplicationContext context: Context
    ) = context

    @Provides
    fun provideBaseUrl(): String = "https://vmakdsocialnetwork.herokuapp.com/"

    @Provides
    @Singleton
    fun provideRequestInterceptor(): RequestTokenInterceptor =
        RequestTokenInterceptor()

    @Provides
    fun provideOkHttpClient(requestTokenInterceptor: RequestTokenInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(requestTokenInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(BASE_URL: String,client: OkHttpClient): Retrofit{

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideAuthRepository(
        authService: AuthService,
        mySocialNetworkDAO: MySocialNetworkDAO
    ): AuthRepositoryImpl = AuthRepositoryImpl(authService, mySocialNetworkDAO)

    @Provides
    @Singleton
    fun provideDataService(retrofit: Retrofit): DataService =
        retrofit.create(DataService::class.java)

    @Provides
    @Singleton
    fun provideDataRepository(
        dataService: DataService,
        mySocialNetworkDAO: MySocialNetworkDAO
    ): DataRepositoryImpl = DataRepositoryImpl(dataService, mySocialNetworkDAO)

    @Provides
    @Singleton
    fun provideVerifyService(retrofit: Retrofit): VerifyService =
        retrofit.create(VerifyService::class.java)

    @Provides
    @Singleton
    fun provideVerifyRepository(
        verifyServiceService: VerifyService,
    ): VerifyRepositoryImpl = VerifyRepositoryImpl(verifyServiceService)


    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): MySocialNetworkDatabase {
        return Room.databaseBuilder(
            appContext,
            MySocialNetworkDatabase::class.java,
            DATABASE_NAME
        ).build()
    }


    @InstallIn(SingletonComponent::class)
    @Module
    class DatabaseModule {
        @Provides
        fun provideMySocialNetworkDao(appDatabase: MySocialNetworkDatabase): MySocialNetworkDAO {
            return appDatabase.mySocialNetworkDAO()
        }
    }


}