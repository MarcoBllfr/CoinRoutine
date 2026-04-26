package org.coinroutine.project.di

import androidx.room.RoomDatabase
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android
import org.coinroutine.project.core.database.portfolio.PortfolioDatabase
import org.coinroutine.project.core.database.portfolio.getPortfolioDatabaseBuilder
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule = module{
    single<HttpClientEngine>{Android.create()}
    singleOf(::getPortfolioDatabaseBuilder).bind<RoomDatabase.Builder<PortfolioDatabase>>()
}
