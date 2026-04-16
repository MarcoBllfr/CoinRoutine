package org.coinroutine.project.coins.domain.api

import org.coinroutine.project.coins.data.remote.dto.CoinDetailsResponseDto
import org.coinroutine.project.coins.data.remote.dto.CoinPriceHistoryResponseDto
import org.coinroutine.project.coins.data.remote.dto.CoinsResponseDto
import org.coinroutine.project.core.domain.DataError
import org.coinroutine.project.core.domain.Result

interface CoinsRemoteDataSource {
    suspend fun getListOfCoins(): Result<CoinsResponseDto, DataError.Remote>
    suspend fun getPriceHistory(coinId: String): Result<CoinPriceHistoryResponseDto, DataError.Remote>
    suspend fun getCoinById(coinId: String): Result<CoinDetailsResponseDto, DataError.Remote>
}