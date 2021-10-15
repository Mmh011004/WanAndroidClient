package com.example.wanandroidclient.app.weight.loadCallback

import com.example.wanandroidclient.R
import com.kingja.loadsir.callback.Callback

/**
 * 作者　: mmh
 * 时间　: 2021/10/12
 * 描述　:
 */
class ErrorCallback : Callback() {
    override fun onCreateView(): Int {
        return R.layout.layout_error
    }

}