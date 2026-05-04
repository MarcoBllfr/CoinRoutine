package org.coinroutine.project.trade.presentation.buy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.coinroutine.project.trade.presentation.common.TradeScreen
import org.coinroutine.project.trade.presentation.common.TradeType
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BuyScreen(
    coinId: String,
    navigateToPortfolio:()-> Unit,
){
    val viewModel = koinViewModel<BuyViewModel>()
    val state  by viewModel.state.collectAsStateWithLifecycle()

    TradeScreen(
        state = state,
        tradeType = TradeType.BUY,
        onAmountChange = viewModel::onAmountChanged,
        onSubmitClicked = viewModel::onBuyClicked
    )

}