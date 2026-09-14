package com.example.S8107253assingment2

import org.koin.dsl.module
import retrofit2.Retrofit
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import org.koin.androidx.viewmodel.dsl.viewModel
val appModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://nit3213apinew.onrender.com/")
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .addLast(KotlinJsonAdapterFactory())
                        .build()
                )
            )
            .build()
    }

    single<ApiService> {
        get<Retrofit>().create(ApiService::class.java)
    }
    single<AuthRepository> { NetworkAuthRepository(get()) }
    single<DashboardRepository> { NetworkDashboardRepository(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { DashboardViewModel(get()) }
}