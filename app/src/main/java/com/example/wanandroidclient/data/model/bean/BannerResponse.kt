package com.example.wanandroidclient.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 作者　: mmh
 * 时间　: 2021/10/16
 * 描述　:
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class BannerResponse(
    var desc : String = "",
    var id: Int = 0,
    var imagePath: String = "",
    var isVisible: Int = 0,
    var order: Int = 0,
    var title: String = "",
    var type: Int = 0,
    var url: String = ""
) : Parcelable
