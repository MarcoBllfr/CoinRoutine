package org.coinroutine.project.trade.presentation.sell

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.coinroutine.project.coins.domain.GetCoinDetailsUseCase
import org.coinroutine.project.core.domain.Result
import org.coinroutine.project.core.util.formatFiat
import org.coinroutine.project.core.util.toUiText
import org.coinroutine.project.portfolio.domain.PortfolioRepository
import org.coinroutine.project.trade.domain.SellCoinUseCase
import org.coinroutine.project.trade.mapper.toCoin
import org.coinroutine.project.trade.presentation.common.TradeState
import org.coinroutine.project.trade.presentation.common.UiTradeCoinItem

class SellViewModel(
    private val getCoinDetailsUseCase: GetCoinDetailsUseCase,
    private val portfolioRepository: PortfolioRepository,
    private val sellCoinUseCase: SellCoinUseCase
): ViewModel() {

    private val tempCoinId = "1" //remove this later and replace by parameter

    private val _amount = MutableStateFlow("")
    private val _state = MutableStateFlow(TradeState())

    val state = combine(
        _state,
        _amount,
    ){
            state,amount -> state.copy(
        amount=amount
    )
    }.onStart {
       when(val portfolioCoinResponse = portfolioRepository.getPortfolioCoin(tempCoinId)){
            is Result.Success ->{
                portfolioCoinResponse.data?.ownedAmountInUnit?.let {
                    getCoinDetails(it)
                }
            }
            is Result.Error -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error= portfolioCoinResponse.error.toUiText()
                    )
                }

            }
        }
    }.stateIn(
        scope = viewModelScope,
        started= SharingStarted.WhileSubscribed(),
        initialValue = TradeState(isLoading = true)
    )
    fun onAmountChanged(amount: String){
        _amount.value= amount
    }
    private suspend fun getCoinDetails(ownedAmountInUnit: Double){
        when(val coinResponse = getCoinDetailsUseCase.execute(tempCoinId)){
            is Result.Success ->{
                val availableAmountInFiat = ownedAmountInUnit * coinResponse.data.price
                _state.update {
                    it.copy(
                        coin = UiTradeCoinItem(
                            id = coinResponse.data.coin.id,
                            name = coinResponse.data.coin.name,
                            symbol = coinResponse.data.coin.symbol,
                            iconUrl = coinResponse.data.coin.iconUrl,
                            price = coinResponse.data.price
                        ),
                        availableAmount = "Available ${formatFiat(availableAmountInFiat)}"
                    )
                }
            }
            is Result.Error ->{
                   _state.update {
                       it.copy(
                           isLoading = false,
                           error= coinResponse.error.toUiText()
                       )
                   }
            }
        }
    }
    fun onSellClicked(){
        val tradeCoin = state.value.coin?: return
        viewModelScope.launch {
            val sellCoinResponse = sellCoinUseCase.sellCoin(
                coin = tradeCoin.toCoin(),
                amountInFiat = _amount.value.toDouble(),
                price = tradeCoin.price
            )
            when(sellCoinResponse){
                is Result.Success -> {
                    //add event

                }
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = sellCoinResponse.error.toUiText()
                        )
                    }
                }
            }
        }
    }
}