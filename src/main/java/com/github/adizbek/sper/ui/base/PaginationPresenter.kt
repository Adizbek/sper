package com.github.adizbek.sper.ui.base

import com.blankj.utilcode.util.ToastUtils
import com.github.adizbek.sper.net.response.BaseResponse
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import net.grandcentrix.thirtyinch.TiPresenter


abstract class PaginationPresenter<V : PaginationView, ITEM : AbstractItem<*, *>> : TiPresenter<V>() {
    val itemAdapter = ItemAdapter<ITEM>()
    val fastAdapter = FastAdapter.with<AbstractItem<*, *>, ItemAdapter<*>>(itemAdapter)

    protected abstract fun getCall(page: Int): Observable<BaseResponse<ITEM>>


    init {
        fastAdapter.setHasStableIds(true)
    }

    private var call: Observable<*>? = null

    fun loadPage(page: Int) {
        call = getCall(page).apply {
            subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it.success) {
                            itemAdapter.add(it.data)
                        }
                    }, {
                        sendToView {
                            it.getRefresh().isRefreshing = false
                        }

                        ToastUtils.showLong(it.localizedMessage)
                    }, {
                        sendToView {
                            it.getRefresh().isRefreshing = false
                        }
                    }, {
                        sendToView {
                            it.getRefresh().post {
                                it.getRefresh().isRefreshing = true
                            }
                        }
                    })
        }
    }

}