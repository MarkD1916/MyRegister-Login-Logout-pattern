package com.vmakd1916gmail.com.mysocialnetwork.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.vmakd1916gmail.com.mysocialnetwork.DB.MySocialNetworkDAO
import com.vmakd1916gmail.com.mysocialnetwork.DB.MySocialNetworkDatabase
import com.vmakd1916gmail.com.mysocialnetwork.R
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.AuthRepositoryImpl
import com.vmakd1916gmail.com.mysocialnetwork.repositories.data.DataRepositoryImpl
import com.vmakd1916gmail.com.mysocialnetwork.repositories.verify.VerifyRepositoryImpl
import com.vmakd1916gmail.com.mysocialnetwork.services.AuthService
import com.vmakd1916gmail.com.mysocialnetwork.services.DataService
import com.vmakd1916gmail.com.mysocialnetwork.services.VerifyService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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

    @Singleton
    @Provides
    fun providePicassoInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_round_hourglass_bottom_24)
            .error(R.drawable.ic_baseline_error_outline_24)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
    )

    @Provides
    fun providesBaseUrl(): String = "https://vmakdsocialnetwork.herokuapp.com/"

    @Provides
    @Singleton
    fun provideRetrofit(BASE_URL: String): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

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
        authService: AuthService,
        dataService: DataService,
        mySocialNetworkDAO: MySocialNetworkDAO
    ): DataRepositoryImpl = DataRepositoryImpl(dataService,authService, mySocialNetworkDAO)

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