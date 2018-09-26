package com.hobbajt.bubblequiz.options.view

import com.hobbajt.bubblequiz.mvp.BasePresenter
import com.hobbajt.bubblequiz.sharedprefs.SharedPreferencesEditor
import javax.inject.Inject

class OptionsPresenter @Inject constructor(private val sharedPreferencesEditor: SharedPreferencesEditor) : BasePresenter<OptionsContractor.View>()
{
    fun onViewCreated()
    {
        view?.displayUI()
    }

    fun onRemoveDataClicked()
    {
        sharedPreferencesEditor.clear()
        view?.displayUserDataRemoved()
    }
}
