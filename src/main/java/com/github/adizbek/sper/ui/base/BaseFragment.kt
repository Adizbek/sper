package com.github.adizbek.sper.ui.base

import android.app.ProgressDialog
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.adizbek.sper.helper.Helper
import net.grandcentrix.thirtyinch.TiFragment
import net.grandcentrix.thirtyinch.TiPresenter
import net.grandcentrix.thirtyinch.TiView

abstract class BaseFragment<P : TiPresenter<V>, V : TiView> : TiFragment<P, V>(), TiView, BaseView {

    var loadingView: ProgressDialog? = null

    // TODO ask is back fragment
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()

                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onStart() {
        super.onStart()

        setHasOptionsMenu(true)
    }

    override fun showLoading() {
        if (loadingView != null) return

        loadingView = ProgressDialog.show(context, "",
                "Загрузка...", true)

        loadingView?.show()
    }

    override fun hideLoading() {
        if (loadingView == null) return

        loadingView?.dismiss()
        loadingView = null
    }

    fun act(): AppCompatActivity {
        return activity as AppCompatActivity
    }

    fun fm(): androidx.fragment.app.FragmentManager {
        return act().supportFragmentManager
    }

    fun setToolbar(title: String, toolbar: View, back: Boolean = true) {
        if (back)
            Helper.setupToolbarBack(act(), toolbar, title)
        else
            Helper.setupToolbar(act(), toolbar, title)
    }


    fun setToolbar(title: Int, toolbar: View, back: Boolean = true) {
        if (back)
            Helper.setupToolbarBack(act(), toolbar, title)
        else
            Helper.setupToolbar(act(), toolbar, title)
    }
}

fun androidx.fragment.app.Fragment.fm(): androidx.fragment.app.FragmentManager {
    return (activity as AppCompatActivity).supportFragmentManager
}
