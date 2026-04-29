package org.coinroutine.project.portfolio.data

import kotlinx.datetime.Clock
import org.coinroutine.project.core.domain.coin.Coin
import org.coinroutine.project.portfolio.data.local.PortfolioCoinEntity
import org.coinroutine.project.portfolio.domain.PortfolioCoinModel

fun PortfolioCoinEntity.toPortfolioCoinModel(
    currentPrice: Double,
): PortfolioCoinModel {
    return PortfolioCoinModel(
        coin = Coin(
            id = coinId,
            name = name,
            symbol = symbol,
            iconUrl = iconUrl
        ),
        performancePercent = ((currentPrice - averagePurchasePrice) / averagePurchasePrice) * 100,
        averagePurchasePrice = averagePurchasePrice,
        ownedAmountInUnit = amountOwned,
        ownedAmountInFiat = amountOwned * currentPrice
    )
}

fun PortfolioCoinModel.toPortfolioCoinEntity(): PortfolioCoinEntity {
    return PortfolioCoinEntity(
        coinId = coin.id,
        name = coin.name,
        symbol = coin.symbol,
        iconUrl = coin.iconUrl,
        amountOwned = ownedAmountInUnit,
        averagePurchasePrice = averagePurchasePrice,
        timestamp = Clock.System.now().toEpochMilliseconds()
    )
}