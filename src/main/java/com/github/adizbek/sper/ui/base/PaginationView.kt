package com.github.adizbek.sper.ui.base

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.items.AbstractItem
import com.mikepenz.fastadapter.listeners.OnClickListener

interface PaginationView : BaseView {

    fun getList(): androidx.recyclerview.widget.RecyclerView

    fun getOnItemClickListener(): OnClickListener<AbstractItem<*, *>>? {
        return null
    }

    fun getRefresh(): androidx.swiperefreshlayout.widget.SwipeRefreshLayout

    fun initList()

    fun addDecorator(): Boolean {
        return false
    }

    fun startPagination();

}