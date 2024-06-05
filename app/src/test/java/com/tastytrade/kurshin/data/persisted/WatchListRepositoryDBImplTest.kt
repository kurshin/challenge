package com.tastytrade.kurshin.data.persisted

import com.tastytrade.kurshin.data.persisted.dao.WatchListDao
import com.tastytrade.kurshin.data.persisted.entity.WatchListEntity
import com.tastytrade.kurshin.domain.WatchList
import com.tastytrade.kurshin.domain.irepository.IWatchListRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class WatchListRepositoryDBImplTest {

    private lateinit var watchlistDao: WatchListDao
    private lateinit var watchListRepository: IWatchListRepository

    @Before
    fun setup() {
        watchlistDao = mockk()
        watchListRepository = WatchListRepositoryDBImpl(watchlistDao)
    }

    @Test
    fun `getAllWatchLists returns list of watchlists`() = runTest {
        // Arrange
        val watchListEntities = listOf(
            WatchListEntity(1L, "Default Watchlist", true),
            WatchListEntity(2L, "Personal Watchlist", false)
        )
        val expectedWatchLists = listOf(
            WatchList("Default Watchlist", true, 1L),
            WatchList("Personal Watchlist",  false, 2L)
        )

        coEvery { watchlistDao.getAllWatchLists() } returns flowOf(watchListEntities)

        // Act
        val result = watchListRepository.getAllWatchLists()

        // Assert
        result.collect { watchLists ->
            assertEquals(expectedWatchLists, watchLists)
        }
        coVerify { watchlistDao.getAllWatchLists() }
    }

    @Test
    fun `getAllWatchListsSync returns list of watchlists`() = runTest {
        // Arrange
        val watchListEntities = listOf(
            WatchListEntity(1L, "Default Watchlist", true),
            WatchListEntity(2L, "Personal Watchlist", false)
        )
        val expectedWatchLists = listOf(
            WatchList("Default Watchlist", true, 1L),
            WatchList("Personal Watchlist",  false, 2L)
        )

        coEvery { watchlistDao.getAllWatchListsSync() } returns watchListEntities

        // Act
        val result = watchListRepository.getAllWatchListsSync()

        // Assert
        assertEquals(expectedWatchLists, result)
        coVerify { watchlistDao.getAllWatchListsSync() }
    }

    @Test
    fun `addWatchlist inserts new watchlist and returns id`() = runTest {
        // Arrange
        val newWatchList = WatchList("New Watchlist", false, 0L)
        val watchListEntity = WatchListEntity(0L, "New Watchlist", false)
        coEvery { watchlistDao.insertWatchList(watchListEntity) } returns 1L

        // Act
        val result = withContext(Dispatchers.IO) { watchListRepository.addWatchlist(newWatchList) }

        // Assert
        assertEquals(1L, result)
        coVerify { watchlistDao.insertWatchList(watchListEntity) }
    }

    @Test
    fun `updateWatchlist updates existing watchlist`() = runTest {
        // Arrange
        val updatedWatchList = WatchList("Updated Watchlist", false, 1L)
        val watchListEntity = WatchListEntity(1L, "Updated Watchlist", false)
        coEvery { watchlistDao.updateWatchList(watchListEntity) } just Runs

        // Act
        withContext(Dispatchers.IO) { watchListRepository.updateWatchlist(updatedWatchList) }

        // Assert
        coVerify { watchlistDao.updateWatchList(watchListEntity) }
    }

    @Test
    fun `removeWatchlist deletes watchlist`() = runTest {
        // Arrange
        val watchListToRemove = WatchList("Remove Watchlist", false, 1L)
        val watchListEntity = WatchListEntity(1L, "Remove Watchlist", false)
        coEvery { watchlistDao.deleteWatchList(watchListEntity) } just Runs

        // Act
        withContext(Dispatchers.IO) { watchListRepository.removeWatchlist(watchListToRemove) }

        // Assert
        coVerify { watchlistDao.deleteWatchList(watchListEntity) }
    }
}
