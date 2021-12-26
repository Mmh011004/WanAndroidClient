package com.example.wanandroidclient.viewmodel.state

import com.example.jetpackmvvm.base.viewmodel.BaseViewModel
import com.example.jetpackmvvm.callback.databind.IntObservableField
import com.example.jetpackmvvm.callback.databind.StringObservableField
import com.example.wanandroidclient.app.util.ColorUtil

/**
 * 作者　: mmh
 * 时间　: 2021/11/3
 * 描述　:
 */
class MeViewModel:BaseViewModel() {

    var name = StringObservableField("请先登录~")

    var integral = IntObservableField(0)

    var info = StringObservableField("id：--　排名：-")

    var imageUrl = StringObservableField(ColorUtil.randomImage())
}