package org.coinroutine.project.trade.presentation.sell

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.ktor.http.parametersOf
import org.coinroutine.project.trade.presentation.common.TradeScreen
import org.coinroutine.project.trade.presentation.common.TradeType
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SellScreen(
    coinId: String,
    navigateToPortfolio:()-> Unit,
){
    val viewModel = koinViewModel< SellViewModel>(
        parameters = {
            parametersOf(coinId)
        }
    )
    val state  by viewModel.state.collectAsStateWithLifecycle()

    TradeScreen(
        state = state,
        tradeType = TradeType.SELL,
        onAmountChange = viewModel::onAmountChanged,
        onSubmitClicked = viewModel::onSellClicked

    )

}