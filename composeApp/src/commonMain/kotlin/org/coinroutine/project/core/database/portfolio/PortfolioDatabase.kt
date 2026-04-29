package org.coinroutine.project.core.database.portfolio

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import org.coinroutine.project.portfolio.data.local.PortfolioCoinEntity
import org.coinroutine.project.portfolio.data.local.PortfolioDao
import org.coinroutine.project.portfolio.data.local.UserBalanceDao
import org.coinroutine.project.portfolio.data.local.UserBalanceEntity

@Database(entities = [PortfolioCoinEntity::class, UserBalanceEntity::class], version = 2)
abstract class PortfolioDatabase : RoomDatabase(){
    abstract fun portfolioDao(): PortfolioDao
    abstract fun userBalanceDao(): UserBalanceDao

}