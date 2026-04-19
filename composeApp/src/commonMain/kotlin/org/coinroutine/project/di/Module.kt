package org.coinroutine.project.di

import io.ktor.client.HttpClient
import org.coinroutine.project.core.network.HttpClientFactory
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(config: KoinAppDeclaration? = null ) =
    startKoin {
        config?.invoke(this)
        modules(
            sharedModule,
            platformModule
        )
    }

expect val platformModule: Module

//for the shared dependency
val sharedModule = module {
    single<HttpClient>{ HttpClientFactory.create(get()) }
}