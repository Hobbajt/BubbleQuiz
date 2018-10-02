package com.hobbajt.bubblequiz.options.view

import com.hobbajt.bubblequiz.mvp.BasePresenter
import com.hobbajt.bubblequiz.sharedprefs.LocalDataEditor
import javax.inject.Inject

class OptionsPresenter @Inject constructor(private val localDataEditor: LocalDataEditor) : BasePresenter<OptionsContract.View>()
{
    fun onViewCreated()
    {
        view?.displayUI()
    }

    fun onRemoveDataClicked()
    {
        localDataEditor.clear()
        view?.displayUserDataRemoved()
    }
}
