package com.example.wanandroidclient.app.weight.loadCallback

import android.content.Context
import android.view.View
import com.example.wanandroidclient.R
import com.kingja.loadsir.callback.Callback

/**
 * 作者　: mmh
 * 时间　: 2021/10/12
 * 描述　:
 */
class LoadingCallback : Callback() {
    override fun onCreateView(): Int {
        return R.layout.layout_loading
    }

    //retry
    override fun onReloadEvent(context: Context?, view: View?): Boolean {
        return true
    }
}