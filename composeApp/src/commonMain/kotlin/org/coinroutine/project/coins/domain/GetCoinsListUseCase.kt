package org.coinroutine.project.coins.domain

import org.coinroutine.project.coins.data.mapper.toCoinModel
import org.coinroutine.project.coins.domain.api.CoinsRemoteDataSource
import org.coinroutine.project.coins.domain.model.CoinModel
import org.coinroutine.project.core.domain.DataError
import org.coinroutine.project.core.domain.Result
import org.coinroutine.project.core.domain.map

class GetCoinsListUseCase(
    private val client: CoinsRemoteDataSource
) {
    suspend fun execute(): Result<List<CoinModel>, DataError.Remote>{
        return client.getListOfCoins().map { dto -> dto.data.coins.map { it.toCoinModel() } }
    }
}