package com.example.wanandroidclient.app.network

import com.example.wanandroidclient.data.model.bean.ApiPagerResponse
import com.example.wanandroidclient.data.model.bean.ApiResponse
import com.example.wanandroidclient.data.model.bean.AriticleResponse
import com.example.wanandroidclient.data.model.bean.BannerResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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

    /**
     * 项目分类标题
     */
/*    @GET("project/tree/json")
    suspend fun getProjecTitle(): ApiResponse<ArrayList<ClassifyResponse>>*/

    /**
     * 根据分类id获取项目数据
     */
    @GET("project/list/{page}/json")
    suspend fun getProjecDataByType(
        @Path("page") pageNo: Int,
        @Query("cid") cid: Int
    ): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>>

    /**
     * 获取最新项目数据
     */
    @GET("article/listproject/{page}/json")
    suspend fun getProjecNewData(@Path("page") pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>>

}