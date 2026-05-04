package org.coinroutine.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.coinroutine.project.coins.presentation.CoinsListScreen
import org.coinroutine.project.core.navigation.Buy
import org.coinroutine.project.core.navigation.Coins
import org.coinroutine.project.core.navigation.Portfolio
import org.coinroutine.project.core.navigation.Sell
import org.coinroutine.project.portfolio.presentation.PortfolioScreen
import org.coinroutine.project.theme.CoinRoutineTheme
import org.coinroutine.project.trade.presentation.buy.BuyScreen
import org.coinroutine.project.trade.presentation.sell.SellScreen

@Composable
fun App() {
    val navController: NavHostController = rememberNavController()
    CoinRoutineTheme{

        NavHost(
            navController = navController,
            startDestination = Portfolio,
            modifier = Modifier.fillMaxSize()
        ){
            //navigation nodes
            composable<Portfolio>{
                PortfolioScreen(
                    onCoinItemClicked = {
                        coinId -> navController.navigate(Sell(coinId))
                    },
                    onDiscoverCoinsClicked = {
                        navController.navigate(Coins)
                    }
                )
            }

            composable <Coins>{
                CoinsListScreen{
                        coinId -> navController.navigate(Buy(coinId))
                }
            }

            composable<Buy> {
                navBackStackEntry ->
                val coinId: String=navBackStackEntry.toRoute<Buy>().coinId
                BuyScreen(
                    coinId = coinId,
                    navigateToPortfolio = {
                        navController.navigate(Portfolio){
                            popUpTo(Portfolio){inclusive=true}
                        }
                    }
                )
            }
            composable<Sell> {
                    navBackStackEntry ->
                val coinId: String = navBackStackEntry.toRoute<Sell>().coinId
                SellScreen(
                    coinId = coinId,
                    navigateToPortfolio = {
                        navController.navigate(Portfolio){
                            popUpTo(Portfolio){inclusive=true}
                        }
                    }
                )
            }


        }

    }
}