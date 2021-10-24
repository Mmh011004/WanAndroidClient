package com.example.wanandroidclient.data.repository.request

import com.example.wanandroidclient.app.network.ApiService
import com.example.wanandroidclient.app.network.apiService
import com.example.wanandroidclient.app.util.CacheUtil
import com.example.wanandroidclient.data.model.bean.ApiPagerResponse
import com.example.wanandroidclient.data.model.bean.ApiResponse
import com.example.wanandroidclient.data.model.bean.AriticleResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

/**
 * 作者　: mmh
 * 时间　: 2021/10/16
 * 描述　:处理线程的封装类
 */

val HttpRequestCoroutine : HttpRequestManger by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED){
    HttpRequestManger()
}

class HttpRequestManger{
    /*
    * 获取首页文章数据*/
    /**
     * @description:    获取首页文章数据
     * @param:          pageNo 页码数
     * @return:         ApiResponse
     */
    suspend fun getHomeData(pageNo : Int): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>>{
        //同时异步请求2个接口，请求完成后合并数据
        return withContext(Dispatchers.IO) {
            val listData = async { apiService.getAritrilList(pageNo) }
            //如果App配置打开了首页请求置顶文章，且是第一页
            if (CacheUtil.isNeedTop() && pageNo == 0) {
                val topData = async { apiService.getTopAritrilList() }
                listData.await().data.datas.addAll(0, topData.await().data)
                listData.await()
            } else {
                listData.await()
            }
        }
    }

    /**
     * 获取项目标题数据
     */
    suspend fun getProjectData(
        pageNo: Int,
        cid: Int = 0,
        isNew: Boolean = false
    ): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>> {
        return if (isNew) {
            apiService.getProjectNewData(pageNo)
        } else {
            apiService.getProjectDataByType(pageNo, cid)
        }
    }
}