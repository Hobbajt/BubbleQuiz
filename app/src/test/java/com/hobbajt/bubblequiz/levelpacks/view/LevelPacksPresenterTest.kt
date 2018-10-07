package com.hobbajt.bubblequiz.levelpacks.view

import com.hobbajt.bubblequiz.levelpacks.model.LevelPacksApiLoader
import com.hobbajt.bubblequiz.levelpacks.model.dto.LevelsPack
import com.hobbajt.bubblequiz.sharedprefs.LocalDataEditor
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class LevelPacksPresenterTest
{
    companion object
    {
        private val TOTAL_PASSED_LEVELS_COUNT = 2
    }

    @Mock
    lateinit var view: LevelPacksContract.View

    @Mock
    lateinit var levelPacksApiLoader: LevelPacksApiLoader

    @Mock
    lateinit var localDataEditor: LocalDataEditor

    @Mock
    lateinit var levelPackHolderView: LevelPackHolderView

    lateinit var presenter: LevelPacksPresenter
    private val levelPacks = listOf(LevelsPack(0, 5, 2, 2),
                                    LevelsPack(1, 5, 2, 5),
                                    LevelsPack(2, 5, 3, 1))

    @Before
    fun setUp()
    {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        Mockito.doReturn(Observable.fromArray(emptyArray<LevelsPack>())).`when`(levelPacksApiLoader).load()
        Mockito.doReturn(TOTAL_PASSED_LEVELS_COUNT).`when`(localDataEditor).readTotalPassedLevelsCount()

        presenter = Mockito.spy(LevelPacksPresenter(levelPacksApiLoader, localDataEditor))
        presenter.attachView(view)
    }

    @Test
    fun attachViewTest()
    {
        presenter.detachView()

        assertNull(presenter.view)
        presenter.attachView(view)
        assertNotNull(presenter.view)
    }

    @Test
    fun detachViewTest()
    {
        presenter.detachView()

        presenter.attachView(view)
        assertNotNull(presenter.view)

        presenter.detachView()
        assertNull(presenter.view)
    }

    @Test
    fun onRetryButtonClickedTest()
    {
        presenter.onRetryButtonClicked()
        verify(presenter, times(1)).loadLevelPacks()
    }

    @Test
    fun onViewCreatedTest()
    {
        presenter.onViewCreated(null)
        verify(presenter, times(1)).loadLevelPacks()

        presenter.onViewCreated(levelPacks)
        verify(presenter, times(1)).onLoadLevelPacksSuccess(levelPacks)
    }

    @Test
    fun onLoadLevelPacksErrorTest()
    {
        presenter.onLoadLevelPacksError()
        verify(view, times(1)).displayDownloadErrorDialog()
    }

    @Test
    fun onLoadLevelPacksSuccessTest()
    {
        presenter.onLoadLevelPacksSuccess(levelPacks)
        verify(view, times(1)).displayData()
    }

    @Test
    fun getItemsCountTest()
    {
        presenter.onLoadLevelPacksSuccess(levelPacks)
        assertEquals(presenter.getItemsCount(), levelPacks.size)
    }

    @Test
    fun onBindLevelPackViewAtPositionTest()
    {
        presenter.loadTotalPassedLevelsCount()
        presenter.onLoadLevelPacksSuccess(levelPacks)

        presenter.onBindLevelPackViewAtPosition(levelPackHolderView, 0)
        verify(levelPackHolderView, times(1)).displayUnlocked(2, 5)

        presenter.onBindLevelPackViewAtPosition(levelPackHolderView, 1)
        verify(levelPackHolderView, times(1)).displayComplete()

        presenter.onBindLevelPackViewAtPosition(levelPackHolderView, 2)
        verify(levelPackHolderView, times(3)).bind()
        verify(levelPackHolderView, times(3)).displayDefault()

        verify(levelPackHolderView, times(1)).displayLocked(3, TOTAL_PASSED_LEVELS_COUNT)
    }

    @Test
    fun onLevelPackClickedTest()
    {
        presenter.loadTotalPassedLevelsCount()
        presenter.onLoadLevelPacksSuccess(levelPacks)

        presenter.onLevelPackClicked(2, levelPackHolderView)
        verify(levelPackHolderView, times(1)).displayLockedAnimation()

        presenter.onLevelPackClicked(0, levelPackHolderView)
        verify(levelPackHolderView, times(1)).displayUnlockedAnimation()
        verify(view, times(1)).openLevels(levelPacks[0])
    }

    @Test
    fun onSaveStateTest()
    {
        presenter.onLoadLevelPacksSuccess(levelPacks)
        assertEquals(presenter.onSaveState(), levelPacks)
    }
}