package org.coinroutine.project.portfolio.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import org.coinroutine.project.coins.domain.api.CoinsRemoteDataSource
import org.coinroutine.project.core.domain.DataError
import org.coinroutine.project.core.domain.EmptyResult
import org.coinroutine.project.portfolio.data.local.PortfolioDao
import org.coinroutine.project.portfolio.data.local.UserBalanceDao
import org.coinroutine.project.portfolio.data.local.UserBalanceEntity
import org.coinroutine.project.portfolio.domain.PortfolioRepository
import org.coinroutine.project.core.domain.Result
import org.coinroutine.project.core.domain.onError
import org.coinroutine.project.core.domain.onSuccess
import org.coinroutine.project.portfolio.domain.PortfolioCoinModel

class PortfolioRepositoryImpl (
    private val portfolioDao: PortfolioDao,
    private val userBalanceDao: UserBalanceDao,
    private val coinsRemoteDataSource: CoinsRemoteDataSource
): PortfolioRepository{

    override suspend fun initializeBalance() {
        val cashBalance = userBalanceDao.getCashBalance()
        if (cashBalance == null) {
            userBalanceDao.insertBalance(
                UserBalanceEntity(cashBalance = 10000.0)
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun allPortfolioCoinsFlow(): Flow<Result<List<PortfolioCoinModel>, DataError.Remote>> {
        return portfolioDao.getAllOwnedCoins().flatMapLatest { portfolioCoinsEntities ->
            if (portfolioCoinsEntities.isEmpty()) {
                flow {
                    emit(Result.Success(emptyList<PortfolioCoinModel>()))
                }
            } else {
                flow {
                    coinsRemoteDataSource.getListOfCoins()
                        .onError { error ->
                            emit(Result.Error(error))
                        }
                        .onSuccess { coinsDto ->
                            val portfolioCoins = portfolioCoinsEntities.mapNotNull { portfolioCoinsEntity ->
                                val coin = coinsDto.data.coins.find { it.uuid == portfolioCoinsEntity.coinId }
                                coin?.let {
                                    portfolioCoinsEntity.toPortfolioCoinModel(it.price)
                                }
                            }
                            emit(Result.Success(portfolioCoins))
                        }
                }
            }
        }.catch {
            emit(Result.Error(DataError.Remote.UNKNOWN))
        }
    }

    override suspend fun getPortfolioCoin(coinId: String): Result<PortfolioCoinModel?, DataError.Remote> {
       coinsRemoteDataSource.getCoinById(coinId).onError {
           error -> return Result.Error(error)
       }
           .onSuccess {
               coinDto -> val portfolioCoinEntity=portfolioDao.getCoinById(coinId)
               return if(portfolioCoinEntity != null){
                   Result.Success(portfolioCoinEntity.toPortfolioCoinModel(coinDto.data.coin.price))
               }else{
                   Result.Success(null)
               }
           }
        return Result.Error(DataError.Remote.UNKNOWN)
    }

    override suspend fun savePortfolioCoin(portfolioCoin: PortfolioCoinModel): EmptyResult<DataError.Local> {
        TODO("Not yet implemented")
    }

    override suspend fun removeCoinFromPortfolio(coinId: String) {
        TODO("Not yet implemented")
    }

    override fun calculateTotalPortfolioValue(): Flow<Result<Double, DataError.Remote>> {
        TODO("Not yet implemented")
    }

    override fun totalBalanceFlow(): Flow<Result<Double, DataError.Remote>> {
        TODO("Not yet implemented")
    }

    override fun cashBalanceFlow(): Flow<Double> {
        TODO("Not yet implemented")
    }

    override suspend fun updateCashBalance(newBalance: Double) {
        TODO("Not yet implemented")
    }

}