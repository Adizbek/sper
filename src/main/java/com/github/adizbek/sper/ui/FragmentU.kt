package com.github.adizbek.sper.ui

import androidx.fragment.app.Fragment
import org.greenrobot.eventbus.EventBus

fun Fragment.registerForEvents() {
    EventBus.getDefault().register(this)
}

fun Fragment.unregisterForEvents() {
    EventBus.getDefault().unregister(this)
}

fun Any.broadcastEvent() {
    EventBus.getDefault().post(this)
}