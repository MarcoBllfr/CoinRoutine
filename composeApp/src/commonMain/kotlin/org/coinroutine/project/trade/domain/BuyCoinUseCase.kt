package org.coinroutine.project.trade.domain

import kotlinx.coroutines.flow.first
import org.coinroutine.project.core.domain.DataError
import org.coinroutine.project.core.domain.Result
import org.coinroutine.project.core.domain.EmptyResult
import org.coinroutine.project.core.domain.coin.Coin
import org.coinroutine.project.portfolio.domain.PortfolioCoinModel
import org.coinroutine.project.portfolio.domain.PortfolioRepository

class BuyCoinUseCase(
    private val portfolioRepository: PortfolioRepository
) {
    suspend fun buyCoin(
        coin: Coin,
        amountInFiat: Double,
        price: Double
    ): EmptyResult<DataError>{
        val balance= portfolioRepository.cashBalanceFlow().first()
        if (balance < amountInFiat){
            return Result.Error(DataError.Local.INSUFFICIENT_FUNDS)
        }
        val existingCoinResult=portfolioRepository.getPortfolioCoin(coin.id)
        val existingCoin = when (existingCoinResult){
            is Result.Success -> existingCoinResult.data
            is Result.Error -> return Result.Error(existingCoinResult.error)
        }
        val amountInUnit= amountInFiat / price
        if(existingCoin != null){
            val newAmountOwned = existingCoin.ownedAmountInUnit + amountInUnit
            val newTotalInvestment= existingCoin.ownedAmountInFiat + amountInFiat
            val newAveragePurchasePrice = newTotalInvestment / newAmountOwned
            portfolioRepository.savePortfolioCoin(
                existingCoin.copy(
                    ownedAmountInUnit = newAmountOwned,
                    ownedAmountInFiat = newTotalInvestment,
                    averagePurchasePrice = newAveragePurchasePrice
                )
            )
        }else{
            portfolioRepository.savePortfolioCoin(
                PortfolioCoinModel(
                    coin = coin,
                    performancePercent = 0.0,
                    averagePurchasePrice = price,
                    ownedAmountInFiat = amountInFiat,
                    ownedAmountInUnit = amountInUnit
                )
            )
        }
        portfolioRepository.updateCashBalance(balance-amountInFiat)
        return Result.Success(Unit)
    }
}