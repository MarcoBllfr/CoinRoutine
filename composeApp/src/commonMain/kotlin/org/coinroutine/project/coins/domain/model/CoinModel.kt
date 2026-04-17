package org.coinroutine.project.coins.domain.model

import org.coinroutine.project.core.domain.coin.Coin

data class CoinModel(
    val coin: Coin,
    val price: Double,
    val change: Double,
)
