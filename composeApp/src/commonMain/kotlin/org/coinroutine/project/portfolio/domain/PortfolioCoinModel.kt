package org.coinroutine.project.portfolio.domain

import org.coinroutine.project.core.domain.coin.Coin

data class PortfolioCoinModel(
    val coin: Coin,
    val performancePercent: Double,
    val averagePurchasePrice: Double,
    val ownedAmountInUnit:Double,
    val ownedAmountInFiat: Double
)
