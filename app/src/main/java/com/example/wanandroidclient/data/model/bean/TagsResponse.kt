package com.example.wanandroidclient.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 作者　: mmh
 * 时间　: 2021/10/16
 * 描述　: 文章标签
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class TagsResponse(
    var name:String,
    var url:String
) : Parcelable
