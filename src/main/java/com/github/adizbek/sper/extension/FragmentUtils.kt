package com.github.adizbek.sper.extension

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.blankj.utilcode.util.FragmentUtils
import com.github.adizbek.sper.R

object FUtils {

    fun replace(fm: FragmentManager, f: Fragment, container: Int, addBackStack: Boolean = true) {
        FragmentUtils.replace(fm, f, container, addBackStack,
                R.anim.slide_right_in,
                R.anim.slide_left_out,
                R.anim.slide_left_in,
                R.anim.slide_right_out
        )
    }
}
