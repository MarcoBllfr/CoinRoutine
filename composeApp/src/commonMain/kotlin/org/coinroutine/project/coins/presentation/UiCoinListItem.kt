package org.coinroutine.project.coins.presentation

data class UiCoinListItem(
    val id: String,
    val name: String,
    val symbol: String,
    val iconUrl: String,
    val price: Double,
    val change: Double,
    val formattedPrice: String,
    val formattedChange: String,
    val isPositive: Boolean
)