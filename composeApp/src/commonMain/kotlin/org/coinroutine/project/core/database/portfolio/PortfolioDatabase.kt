package org.coinroutine.project.core.database.portfolio

import androidx.room.Database
import androidx.room.RoomDatabase
import org.coinroutine.project.portfolio.data.local.PortfolioCoinEntity
import org.coinroutine.project.portfolio.data.local.PortfolioDao

@Database(entities = [PortfolioCoinEntity::class], version = 1)
abstract class PortfolioDatabase : RoomDatabase(){
    abstract fun portfolioDao(): PortfolioDao
}