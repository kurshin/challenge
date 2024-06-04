package com.tastytrade.kurshin

import android.content.Context
import androidx.room.Room
import com.tastytrade.kurshin.data.persisted.AppDatabase
import com.tastytrade.kurshin.data.persisted.QuoteRepositoryImpl
import com.tastytrade.kurshin.data.persisted.WatchListRepositoryDBImpl
import com.tastytrade.kurshin.data.persisted.dao.QuoteDao
import com.tastytrade.kurshin.data.persisted.dao.WatchListDao
import com.tastytrade.kurshin.data.remote.createRetrofitInstance
import com.tastytrade.kurshin.data.remote.stock.StockService
import com.tastytrade.kurshin.data.remote.stock.StockSimulationRepositoryImpl
import com.tastytrade.kurshin.data.remote.symbol.SymbolRepositoryImpl
import com.tastytrade.kurshin.data.remote.symbol.SymbolService
import com.tastytrade.kurshin.domain.irepository.IQuoteRepository
import com.tastytrade.kurshin.domain.irepository.IStockRepository
import com.tastytrade.kurshin.domain.irepository.ISymbolRepository
import com.tastytrade.kurshin.domain.irepository.IWatchListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "tastytrade_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideWatchListDao(database: AppDatabase): WatchListDao {
        return database.watchListDao()
    }

    @Provides
    @Singleton
    fun provideQuoteDao(database: AppDatabase): QuoteDao {
        return database.quoteDao()
    }

    @Provides
    @Singleton
    fun provideSymbolService(): SymbolService {
        return createRetrofitInstance<SymbolService>(BuildConfig.BASE_URL_SYMBOL_API).service
    }

    @Provides
    @Singleton
    fun provideStockService(): StockService {
        return createRetrofitInstance<StockService>(BuildConfig.BASE_URL_STOCK_API).service
    }

    @Provides
    @Singleton
    fun provideIStockRepository(): IStockRepository {
        return StockSimulationRepositoryImpl()
    }

    /**
     * All of a sudden IEX stopped working for free on June 1. If you have a paid subscription
     * feel free to uncomment this method and comment method above to use real IEX service.
     */
//    @Provides
//    @Singleton
//    fun provideIStockRepository(stockService: StockService): IStockRepository {
//        return StockRepositoryImpl(stockService)
//    }

    @Provides
    @Singleton
    fun provideIWatchListRepository(watchlistDao: WatchListDao): IWatchListRepository {
        return WatchListRepositoryDBImpl(watchlistDao)
    }

    @Provides
    @Singleton
    fun provideISymbolRepository(symbolService: SymbolService): ISymbolRepository {
        return SymbolRepositoryImpl(symbolService)
    }

    @Provides
    @Singleton
    fun provideIQuoteRepository(quoteDao: QuoteDao): IQuoteRepository {
        return QuoteRepositoryImpl(quoteDao)
    }
}