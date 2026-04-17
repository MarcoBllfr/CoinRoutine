package org.coinroutine.project.coins.domain

import org.coinroutine.project.coins.data.mapper.toPriceModel
import org.coinroutine.project.coins.domain.api.CoinsRemoteDataSource
import org.coinroutine.project.coins.domain.model.PriceModel
import org.coinroutine.project.core.domain.DataError
import org.coinroutine.project.core.domain.Result
import org.coinroutine.project.core.domain.map

class GetCoinPriceHistoryUseCase(
    private val client: CoinsRemoteDataSource
) {
    suspend fun execute(coinId: String): Result<List<PriceModel>, DataError.Remote>{
        return client.getPriceHistory(coinId).map { dto -> dto.history.map { it.toPriceModel() } }
    }
}