package com.example.core.di

import android.content.Context
import androidx.room.Room
import com.example.core.BuildConfig
import com.example.core.data.UserRepository
import com.example.core.data.source.local.LocalDataSource
import com.example.core.data.source.local.preferences.SharedPreferences
import com.example.core.data.source.local.room.UserDatabase
import com.example.core.data.source.remote.RemoteDataSource
import com.example.core.data.source.remote.network.ApiService
import com.example.core.domain.repository.IUserRepository
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    factory { get<UserDatabase>().userDao() }

    single {
        provideUserDatabase(androidContext())
    }
}

fun provideUserDatabase(context: Context): UserDatabase {
    val passphrase: ByteArray = SQLiteDatabase.getBytes("users".toCharArray())
    val factory = SupportFactory(passphrase)

    return Room.databaseBuilder(
        context,
        UserDatabase::class.java,
        "Users.db"
    ).fallbackToDestructiveMigration()
        .openHelperFactory(factory)
        .build()
}


val networkModule = module {
    single { provideAuthInterceptor() }
    single { provideLoggingInterceptor() }
    single { provideCertificatePinner() }
    single { provideOkHttpClient(get(), get(), get()) }
    single { provideRetrofit(get()) }
    single { provideApiService(get()) }
}

fun provideAuthInterceptor(): Interceptor {
    return Interceptor { chain ->
        val req = chain.request()
        val requestHeaders = req.newBuilder()
            .addHeader("Authorization", "token " + BuildConfig.KEY)
            .build()
        chain.proceed(requestHeaders)
    }
}

fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    return if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    } else {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
    }
}

fun provideCertificatePinner(): CertificatePinner {
    val hostname = "api.github.com"
    return CertificatePinner.Builder()
        .add(hostname, "sha256/jFaeVpA8UQuidlJkkpIdq3MPwD0m8XbuCRbJlezysBE=")
        .add(hostname, "sha256/Wec45nQiFwKvHtuHxSAMGkt19k+uPSw9JlEkxhvYPHk=")
        .add(hostname, "sha256/Jg78dOE+fydIGk19swWwiypUSR6HWZybfnJG/8G7pyM=")
        .add(hostname, "sha256/e0IRz5Tio3GA1Xs4fUVWmH1xHDiH2dMbVtCBSkOIdqM=")
        .build()
}

fun provideOkHttpClient(
    loggingInterceptor: HttpLoggingInterceptor,
    authInterceptor: Interceptor,
    certificatePinner: CertificatePinner
): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .connectTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .certificatePinner(certificatePinner)
        .build()
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
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

