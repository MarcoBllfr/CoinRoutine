package org.coinroutine.project.coins.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.coinroutine.project.coins.domain.GetCoinsListUseCase
import org.coinroutine.project.core.domain.Result

class CoinsListViewModel(
    private val getCoinsListUseCase: GetCoinsListUseCase
): ViewModel() {
    private val _state= MutableStateFlow(CoinsState())
    val state= _state
        .onStart {
            getAllCoins()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CoinsState()
        )
    private suspend fun getAllCoins(){
        when (val coinsResponse = getCoinsListUseCase.execute()){
            is Result.Success -> {
               _state.update { CoinsState(
                   coins = coinsResponse.data.map{
                       coinItem ->
                       UiCoinListItem(
                           id = coinItem.coin.id,
                           name = coinItem.coin.name,
                           symbol = coinItem.coin.symbol,
                           iconUrl = coinItem.coin.iconUrl,
                           price = coinItem.price,
                           change = coinItem.change,
                           formattedPrice = coinItem.price.toString(),
                           formattedChange = coinItem.change.toString(),
                           isPositive = coinItem.change>0
                       )
                   }
               ) }
            }
            is Result.Error -> {
                _state.update {
                    it.copy(
                        coins = emptyList(),
                        error = null, //TODO coinsResponse.error.toString() format or .toUiText(),
                    )
                }
            }
        }
    }
}

