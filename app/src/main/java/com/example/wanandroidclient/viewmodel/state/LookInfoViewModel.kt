package com.example.wanandroidclient.viewmodel.state

import com.example.jetpackmvvm.base.viewmodel.BaseViewModel
import com.example.jetpackmvvm.callback.databind.StringObservableField

/**
 * 作者　: mmh
 * 时间　: 2021/10/19
 * 描述　:
 */
class LookInfoViewModel : BaseViewModel() {

    var name =StringObservableField("--")

    //头像图片，网站获取
    var imageUrl =
        StringObservableField("https://upload.jianshu.io/users/upload_avatars/9305757/93322613-ff1a-445c-80f9-57f088f7c1b1.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/300/h/300/format/webp")

    var info = StringObservableField()
}