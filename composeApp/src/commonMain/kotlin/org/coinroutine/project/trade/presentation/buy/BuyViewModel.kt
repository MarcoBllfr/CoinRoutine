package org.coinroutine.project.trade.presentation.buy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.coinroutine.project.coins.domain.GetCoinDetailsUseCase
import org.coinroutine.project.core.domain.Result
import org.coinroutine.project.core.util.formatFiat
import org.coinroutine.project.core.util.toUiText
import org.coinroutine.project.portfolio.domain.PortfolioRepository
import org.coinroutine.project.trade.domain.BuyCoinUseCase
import org.coinroutine.project.trade.mapper.toCoin
import org.coinroutine.project.trade.presentation.common.TradeState
import org.coinroutine.project.trade.presentation.common.UiTradeCoinItem

class BuyViewModel(
    private val getCoinDetailsUseCase: GetCoinDetailsUseCase,
    private val portfolioRepository: PortfolioRepository,
    private val buyCoinUseCase: BuyCoinUseCase,
    private val coinId: String,
): ViewModel() {

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
        val balance = portfolioRepository.cashBalanceFlow().first()
        getCoinDetails(balance)
     }.stateIn(
         scope = viewModelScope,
         started= SharingStarted.WhileSubscribed(),
         initialValue = TradeState(isLoading = true)
     )
    private suspend fun getCoinDetails(balance: Double){
        when(val coinResponse= getCoinDetailsUseCase.execute(coinId)){
            is Result.Success ->{
                _state.update {
                    it.copy(
                        coin = UiTradeCoinItem(
                            id= coinResponse.data.coin.id,
                            name = coinResponse.data.coin.name,
                            symbol = coinResponse.data.coin.symbol,
                            iconUrl = coinResponse.data.coin.iconUrl,
                            price = coinResponse.data.price
                        ),
                        availableAmount = "Available: ${formatFiat(balance) }"
                    )
                }
            }
            is Result.Error -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error= coinResponse.error.toUiText()
                    )
                }
            }
        }
    }
    fun onAmountChanged(amount: String){
        _amount.value= amount
    }

    fun onBuyClicked(){
        val tradeCoin = state.value.coin?: return
        viewModelScope.launch {
            val buyCoinResponse = buyCoinUseCase.buyCoin(
                coin = tradeCoin.toCoin(),
                amountInFiat = _amount.value.toDouble(),
                price = tradeCoin.price
            )
            when(buyCoinResponse){
                is Result.Success ->{
                    //TODO: Navigate to next screen
                }
                is Result.Error ->{
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = buyCoinResponse.error.toUiText()
                        )
                    }
                }
            }
        }
    }
}