package com.github.adizbek.sper.ui.base

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import com.mikepenz.fastadapter.items.AbstractItem
import com.mikepenz.fastadapter.listeners.OnClickListener

interface PaginationView : BaseView {

    fun getList(): RecyclerView

    fun getOnItemClickListener(): OnClickListener<AbstractItem<*, *>>? {
        return null
    }

    fun getRefresh(): SwipeRefreshLayout

    fun initList()

    fun addDecorator(): Boolean {
        return false
    }

    fun startPagination();

}