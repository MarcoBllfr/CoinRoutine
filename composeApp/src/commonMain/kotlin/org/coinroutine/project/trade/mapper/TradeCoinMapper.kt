package org.coinroutine.project.trade.mapper

import org.coinroutine.project.core.domain.coin.Coin
import org.coinroutine.project.trade.presentation.common.UiTradeCoinItem

fun UiTradeCoinItem.toCoin()= Coin(
    id=id,
    name=name,
    symbol=symbol,
    iconUrl=iconUrl
)