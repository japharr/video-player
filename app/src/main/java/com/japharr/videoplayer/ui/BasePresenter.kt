package com.japharr.videoplayer.ui

import android.util.Log
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Japharr on 8/6/2018.
 */
abstract class BasePresenter<T: BaseView> {
    open val TAG = BasePresenter::class.java.simpleName

    protected val disposables = CompositeDisposable()
    protected var view: T? = null

    fun bind(view: T) {
        Log.i(TAG, "binding view")
        this.view = view
    }

    fun unbind() {
        Log.i(TAG, "unbinding view")
        this.view = null
    }

    fun destroy() {
        Log.i(TAG, "destroying view")
        disposables.clear()
        unbind()
    }
}