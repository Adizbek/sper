package com.github.adizbek.sper.ui.base

import net.grandcentrix.thirtyinch.TiView

interface BaseView : TiView {
    fun showLoading()
    fun hideLoading()

    fun showError(err: Throwable, msg: String? = null)
    fun showDialog(msg: String)
}