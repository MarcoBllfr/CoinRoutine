package org.coinroutine.project

import androidx.compose.runtime.*
import org.coinroutine.project.theme.CoinRoutineTheme

@Composable
fun App() {
    CoinRoutineTheme{
        println("LA_MIA_CHIAVE_DEBUG: ${AppConfig.apiKey}")
    }
}