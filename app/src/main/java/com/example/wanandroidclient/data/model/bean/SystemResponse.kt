package com.example.wanandroidclient.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 作者　: mmh
 * 时间　: 2021/10/26
 * 描述　: 体系数据
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class SystemResponse(var children: ArrayList<ClassifyResponse>,
                          var courseId: Int,
                          var id: Int,
                          var name: String,
                          var order: Int,
                          var parentChapterId: Int,
                          var userControlSetTop: Boolean,
                          var visible: Int) : Parcelable
