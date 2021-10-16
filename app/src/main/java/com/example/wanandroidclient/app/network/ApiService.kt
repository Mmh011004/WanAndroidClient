package com.example.wanandroidclient.app.network

import com.example.wanandroidclient.data.model.bean.ApiPagerResponse
import com.example.wanandroidclient.data.model.bean.ApiResponse
import com.example.wanandroidclient.data.model.bean.AriticleResponse
import com.example.wanandroidclient.data.model.bean.BannerResponse
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * 作者　: mmh
 * 时间　: 2021/10/16
 * 描述　:
 */
interface ApiService {
    companion object {
        const val SERVER_URL = "https://wanandroid.com/"
    }

    /**
     * 获取banner数据
     */
    @GET("banner/json")
    suspend fun getBanner(): ApiResponse<ArrayList<BannerResponse>>

    /**
     * 获取置顶文章集合数据
     */
    @GET("article/top/json")
    suspend fun getTopAritrilList(): ApiResponse<ArrayList<AriticleResponse>>

    /**
     * 获取首页文章数据
     */
    @GET("article/list/{page}/json")
    suspend fun getAritrilList(@Path("page") pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>>

}