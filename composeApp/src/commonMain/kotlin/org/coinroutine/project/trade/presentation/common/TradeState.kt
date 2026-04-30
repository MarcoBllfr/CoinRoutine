package org.coinroutine.project.trade.presentation.common

import org.coinroutine.project.core.domain.coin.Coin
import org.jetbrains.compose.resources.StringResource

data class TradeState (
    val isLoading: Boolean = false,
    val error: StringResource?= null,
    val availableAmount: String = "",
    val amount: String = "",
    val coin: UiTradeCoinItem? = null
)