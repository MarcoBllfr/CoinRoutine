package org.coinroutine.project.coins.data.mapper

import org.coinroutine.project.coins.data.remote.dto.CoinItemDto
import org.coinroutine.project.coins.data.remote.dto.CoinPriceDto
import org.coinroutine.project.coins.domain.model.CoinModel
import org.coinroutine.project.coins.domain.model.PriceModel
import org.coinroutine.project.core.domain.coin.Coin

fun CoinItemDto.toCoinModel()= CoinModel(
       coin = Coin(
               id= uuid,
               name=name,
               symbol=symbol,
               iconUrl=iconUrl,
       ),
        price=price,
        change=change
)

fun CoinPriceDto.toPriceModel()= PriceModel(
        price=price?: 0.0,
        timestamp=timestamp,
)