package com.example.core.di

import android.content.Context
import androidx.room.Room
import com.example.core.BuildConfig
import com.example.core.data.UserRepository
import com.example.core.data.source.local.LocalDataSource
import com.example.core.data.source.local.preferences.SharedPreferences
import com.example.core.data.source.local.room.UserDao
import com.example.core.data.source.local.room.UserDatabase
import com.example.core.data.source.remote.RemoteDataSource
import com.example.core.data.source.remote.network.ApiService
import com.example.core.domain.repository.IUserRepository
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val API_KEY = BuildConfig.KEY
private const val BASE_URL = BuildConfig.BASE_URL

val databaseModule = module {
    factory { provideUserDao(get()) }
    single { provideUserDatabase(androidContext()) }
}

private fun provideUserDao(database: UserDatabase): UserDao {
    return database.userDao()
}

private fun provideUserDatabase(context: Context): UserDatabase {
    return Room.databaseBuilder(
        context,
        UserDatabase::class.java, "User.db"
    ).fallbackToDestructiveMigration().build()
}

val networkModule = module {
    factory { provideOkHttpClient() }
    factory { provideRetrofit(get()) }
    single { provideApiService(get()) }
}

fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("Authorization", API_KEY)
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .build()
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
}

fun provideApiService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
}


val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    single { SharedPreferences(get()) }
    single<IUserRepository> {
        UserRepository(
            get(),
            get(),
            get()
        )
    }
}

