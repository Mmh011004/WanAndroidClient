package com.example.wanandroidclient.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.example.jetpackmvvm.base.viewmodel.BaseViewModel
import com.example.jetpackmvvm.ext.request
import com.example.jetpackmvvm.ext.util.logd
import com.example.jetpackmvvm.state.ResultState
import com.example.wanandroidclient.app.network.apiService
import com.example.wanandroidclient.app.network.stateCallback.ListDataUiState
import com.example.wanandroidclient.data.model.bean.AriticleResponse
import com.example.wanandroidclient.data.model.bean.BannerResponse
import com.example.wanandroidclient.data.repository.request.HttpRequestCoroutine

/**
 * 作者　: mmh
 * 时间　: 2021/10/15
 * 描述　: 两种回调方式
 * 1.一个是首页文章列表，将返回数据放到ViewModel中过滤，包装给activity或者fragment
 * 2.另一个是轮播图的，将返回的数据返回给activity和fragment
 */
class RequestHomeViewModel : BaseViewModel(){

    //页码 首页数据页码从0开始
    var pageNo = 0

    //首页文章列表数据
    var homeDataState : MutableLiveData<ListDataUiState<AriticleResponse>> = MutableLiveData()

    //首页轮播图数据
    var bannerData : MutableLiveData<ResultState<ArrayList<BannerResponse>>> = MutableLiveData()

    /**
     * @description:获取首页文章列表
     * @param: isRefresh 是否刷新，即第一页
     * @return:
     */
    fun getHomeData(isRefresh : Boolean){
        if (isRefresh) {
            pageNo = 0
        }
        request({ HttpRequestCoroutine.getHomeData(pageNo)}, {
            //请求数据
            pageNo++
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isRefresh = isRefresh,
                    isEmpty = it.isEmpty(),
                    hasMore = it.hasMore(),
                    isFirstEmpty = isRefresh && it.isEmpty(),
                    listData = it.datas
                )
            homeDataState.value =listDataUiState
            "RequestHomeViewModel: $homeDataState".logd()
        },{
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errorMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<AriticleResponse>()
                )
            homeDataState.value = listDataUiState
        })
    }

    /**
     * 获取轮播图数据
     */
    fun getBannerData() {
        request({ apiService.getBanner() }, bannerData)
    }

}
