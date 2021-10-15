package com.example.wanandroidclient.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 作者　: mmh
 * 时间　: 2021/10/12
 * 描述　: 账户信息
 */
//忽略警告并且可以编译项目
@SuppressLint("ParcelCreator")
//使用Parcelize注解简化Parcelable的书写
@Parcelize
data class UserInfo(var admin: Boolean = false,
                    var chapterTops: List<String> = listOf(),
                    var collectIds: MutableList<String> = mutableListOf(),
                    var email: String="",
                    var icon: String="",
                    var id: String="",
                    var nickname: String="",
                    var password: String="",
                    var token: String="",
                    var type: Int =0,
                    var username: String="") : Parcelable
