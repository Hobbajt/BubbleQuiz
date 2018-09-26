package com.hobbajt.bubblequiz.activity

import android.support.v4.app.FragmentManager
import com.hobbajt.bubblequiz.R
import com.hobbajt.bubblequiz.levels.view.LevelsFragment
import com.hobbajt.bubblequiz.menu.view.MenuFragment
import com.hobbajt.bubblequiz.mvp.BaseMVPFragment
import com.hobbajt.bubblequiz.photo.view.PhotoFragment
import javax.inject.Inject

class FragmentsManager @Inject constructor(private val fragmentManager: FragmentManager)
{
    private val currentFragment: BaseMVPFragment<*>?
        get() = fragmentManager.findFragmentById(R.id.fragment) as BaseMVPFragment<*>?

    fun changeFragment(fragment: BaseMVPFragment<*>, addToBackStack: Boolean)
    {
        val transaction = fragmentManager.beginTransaction()
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
        currentFragment?.let { transaction.remove(it) }
        transaction.add(R.id.fragment, fragment)

        if (addToBackStack)
        {
            transaction.addToBackStack(fragment::class.java.simpleName)
        }

        transaction.commit()
    }

    fun onBackPressed(): Boolean
    {
        if(fragmentManager.backStackEntryCount > 0){

            if(currentFragment is PhotoFragment)
            {
                fragmentManager.popBackStack(LevelsFragment::class.simpleName, 0)
            }
            else
            {
                fragmentManager.popBackStackImmediate()
            }
            return true
        }
        return false
    }

    fun onResume()
    {
        if(currentFragment == null)
            changeFragment(MenuFragment.newInstance(), false)
    }
}