package org.coinroutine.project

import androidx.compose.runtime.*
import org.coinroutine.project.portfolio.presentation.PortfolioScreen
import org.coinroutine.project.theme.CoinRoutineTheme

@Composable
fun App() {
    CoinRoutineTheme{
        PortfolioScreen(
            onCoinItemClicked = {},
            onDiscoverCoinsClicked = {}
        )
    }
}