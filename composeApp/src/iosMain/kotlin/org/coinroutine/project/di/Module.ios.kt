package org.coinroutine.project.di

import androidx.room.RoomDatabase
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.coinroutine.project.core.database.getPortfolioDatabaseBuilder
import org.coinroutine.project.core.database.portfolio.PortfolioDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule= module{
    single<HttpClientEngine>{Darwin.create()}
    singleOf(::getPortfolioDatabaseBuilder).bind<RoomDatabase.Builder<PortfolioDatabase>>()
}
