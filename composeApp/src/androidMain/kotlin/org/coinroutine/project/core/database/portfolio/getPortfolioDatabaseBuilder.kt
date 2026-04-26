package org.coinroutine.project.core.database.portfolio


import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun getPortfolioDatabaseBuilder(context: Context): RoomDatabase.Builder<PortfolioDatabase> {
    val dbFile = context.getDatabasePath("portfolio.db")
    return Room.databaseBuilder<PortfolioDatabase>(
        context = context,
        name = dbFile.absolutePath,
    )
}