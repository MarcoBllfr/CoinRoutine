package org.coinroutine.project.di

import androidx.room.RoomDatabase
import io.ktor.client.HttpClient
import org.coinroutine.project.coins.data.remote.impl.KtorCoinsRemoteDataSource
import org.coinroutine.project.coins.domain.GetCoinDetailsUseCase
import org.coinroutine.project.coins.domain.GetCoinPriceHistoryUseCase
import org.coinroutine.project.coins.domain.GetCoinsListUseCase
import org.coinroutine.project.coins.domain.api.CoinsRemoteDataSource
import org.coinroutine.project.coins.presentation.CoinsListViewModel
import org.coinroutine.project.core.database.portfolio.PortfolioDatabase
import org.coinroutine.project.core.database.portfolio.getPortfolioDatabase
import org.coinroutine.project.core.network.HttpClientFactory
import org.coinroutine.project.portfolio.data.PortfolioRepositoryImpl
import org.coinroutine.project.portfolio.domain.PortfolioRepository
import org.coinroutine.project.portfolio.presentation.PortfolioViewModel
import org.coinroutine.project.trade.domain.BuyCoinUseCase
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
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
    //core connection
    single<HttpClient>{ HttpClientFactory.create(get()) }

    //coins list
    viewModel { CoinsListViewModel(get(), get()) }
    singleOf(::GetCoinsListUseCase)
    singleOf(::KtorCoinsRemoteDataSource).bind<CoinsRemoteDataSource>()
    singleOf(::GetCoinDetailsUseCase)
    singleOf(::GetCoinPriceHistoryUseCase)

    //portfolio
    single {
        getPortfolioDatabase(get<RoomDatabase.Builder<PortfolioDatabase>>())
    }
    singleOf(::PortfolioRepositoryImpl).bind<PortfolioRepository>()
    single { get<PortfolioDatabase>().portfolioDao() }
    single { get<PortfolioDatabase>().userBalanceDao() }
    viewModel { PortfolioViewModel(get()) }

    //trade
    singleOf(::BuyCoinUseCase)
}