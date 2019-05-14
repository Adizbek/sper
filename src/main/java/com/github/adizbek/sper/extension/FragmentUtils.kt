package com.github.adizbek.sper.extension

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.blankj.utilcode.util.FragmentUtils
import com.github.adizbek.sper.R

object FUtils {

    fun replace(fm: androidx.fragment.app.FragmentManager, f: androidx.fragment.app.Fragment, container: Int, addBackStack: Boolean = true) {
        FragmentUtils.replace(fm, f, container, addBackStack,
                R.anim.slide_right_in,
                R.anim.slide_left_out,
                R.anim.slide_left_in,
                R.anim.slide_right_out
        )
    }
}
