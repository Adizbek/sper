package com.github.adizbek.sper.ui.base

import android.os.Bundle
import android.view.View
import com.github.adizbek.sper.helper.Helper
import com.mikepenz.fastadapter_extensions.scroll.EndlessRecyclerOnScrollListener

abstract class PaginationFragment<P, V : PaginationView> : BaseFragment<PaginationPresenter<V, *>, V>(), PaginationView {

    override fun initList() {
        Helper.UI.setupList(getList(), presenter.fastAdapter, activity!!, addDecorator())
        println("List initialized")
    }

    override fun startPagination() {
        val listener = object : EndlessRecyclerOnScrollListener() {
            override fun onLoadMore(currentPage: Int) {
                presenter.loadPage(currentPage)
            }

        }

        getList().addOnScrollListener(listener)


        presenter.fastAdapter.withOnClickListener(getOnItemClickListener())

        listener.resetPageCount(0)

        getRefresh().setOnRefreshListener {
            presenter.itemAdapter.clear()
            listener.resetPageCount(0)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initList()
        startPagination()
    }
}