package org.coinroutine.project.portfolio.domain

import kotlinx.coroutines.flow.Flow
import org.coinroutine.project.core.domain.DataError
import org.coinroutine.project.core.domain.EmptyResult
import org.coinroutine.project.core.domain.Result

interface PortfolioRepository {

    suspend fun initializeBalance()
    fun getPortfolioCoinsFlow(): Flow<Result<List<PortfolioCoinModel>, DataError.Remote>>
    suspend fun getPortfolioCoin(coinId: String): Result<PortfolioCoinModel, DataError.Remote>
    suspend fun savePortfolioCoin(portfolioCoinModel: PortfolioCoinModel): EmptyResult<DataError.Local>
    suspend fun removeCoinFromPortfolio(coinId: String)

    fun calculateTotalPortfolioValue(): Flow<Result<Double, DataError.Remote >>
    fun totalBalanceFlow(): Flow<Result<Double, DataError.Remote>>
    fun cashBalanceFlow(): Flow<Double>
    suspend fun updateCashBalance(newBalance: Double)
}