package com.example.jetpackmvvm.network

/**
 * 作者　: mmh
 * 时间　: 2021/10/16
 * 描述　: 服务器返回数据的基类
 */
abstract class BaseResponse<T> {
    //抽象方法，用户的基类继承该类时，需要重写该方法
    abstract fun isSucces(): Boolean

    abstract fun getResponseData(): T

    abstract fun getResponseCode(): Int

    abstract fun getResponseMsg(): String
}