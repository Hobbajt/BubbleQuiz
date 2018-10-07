package com.hobbajt.bubblequiz.levelpacks.view

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.hobbajt.bubblequiz.BuildConfig
import com.hobbajt.bubblequiz.activity.FragmentsManager
import com.hobbajt.bubblequiz.activity.MainActivity
import com.hobbajt.bubblequiz.levelpacks.model.dto.LevelsPack
import com.nhaarman.mockitokotlin2.*
import junit.framework.Assert
import junit.framework.Assert.assertNotNull
import kotlinx.android.synthetic.main.fragment_level.*
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment


@Config(constants = BuildConfig::class)
@RunWith(RobolectricTestRunner::class)
class LevelPacksFragmentTest
{
    private lateinit var fragment: LevelPacksFragment

    @Mock
    private lateinit var presenter: LevelPacksPresenter

    @Mock
    private lateinit var fragmentsManager: FragmentsManager

    @Before
    fun setUp()
    {
        MockitoAnnotations.initMocks(this)
        fragment = spy(LevelPacksFragment.newInstance())
        startFragment(fragment, MainActivity::class.java)
        fragment.presenter = presenter
        fragment.fragmentsManager = fragmentsManager
    }

    @Test
    fun attachViewTest()
    {
        assertNotNull(fragment)
        verify(fragment, times(1)).onAttach(any<Context>())
    }

    @Test
    fun displayDataTest()
    {
        fragment.displayData()
        assertThat(fragment.rvItems.layoutManager, instanceOf(GridLayoutManager::class.java))
        assertNotNull(fragment.rvItems.adapter)
    }

    @Test
    fun openLevelTest()
    {
        fragment.openLevels(LevelsPack(0, 5, 3, 2))
        verify(fragmentsManager).changeFragment(any(), eq(true))
    }

    @Test
    fun providePresenterTest()
    {
        val presenter = fragment.providePresenter()
        Assert.assertEquals(this.presenter, presenter)
    }

    @Test
    fun onSaveInstanceStateTest()
    {
        val bundle = spy(Bundle())
        fragment.onSaveInstanceState(bundle)
        verify(bundle).putSerializable(eq(LevelPacksFragment.LEVELS_PACKS_TAG), any())
    }
}