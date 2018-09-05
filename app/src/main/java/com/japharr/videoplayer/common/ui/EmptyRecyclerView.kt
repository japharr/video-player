package com.japharr.videoplayer.common.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View

/**
 * Created by Japharr on 8/6/2018.
 */
class EmptyRecyclerView: RecyclerView {
    private var emptyView: View? = null

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs, defStyle)

    val observer = object: AdapterDataObserver() {
        override fun onChanged() {
            checkIfEmpty()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            checkIfEmpty()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            checkIfEmpty()
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        val oldAdapter = getAdapter()
        oldAdapter?.unregisterAdapterDataObserver(observer)
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(observer)

        checkIfEmpty()
    }

    fun setEmptyView(emptyView: View) {
        this.emptyView = emptyView
        checkIfEmpty()
    }

    fun checkIfEmpty() {
        if(emptyView != null && adapter != null) {
            val emptyViewVisible = adapter.itemCount == 0;

            (emptyView as View).visibility = if (emptyViewVisible) View.VISIBLE else View.GONE
            visibility = if (emptyViewVisible) View.GONE else View.VISIBLE
        }
    }
}