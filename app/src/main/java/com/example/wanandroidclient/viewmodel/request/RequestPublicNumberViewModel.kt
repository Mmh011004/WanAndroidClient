package com.example.wanandroidclient.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.example.jetpackmvvm.base.viewmodel.BaseViewModel
import com.example.jetpackmvvm.ext.request
import com.example.jetpackmvvm.state.ResultState
import com.example.wanandroidclient.app.network.apiService
import com.example.wanandroidclient.app.network.stateCallback.ListDataUiState
import com.example.wanandroidclient.data.model.bean.AriticleResponse
import com.example.wanandroidclient.data.model.bean.ClassifyResponse

/**
 * 作者　: mmh
 * 时间　: 2021/11/1
 * 描述　:
 */
class RequestPublicNumberViewModel: BaseViewModel() {
    //页码，从1开始
    var pageNo = 1

    //分类数据，公众号名字
    var titleData : MutableLiveData<ResultState<ArrayList<ClassifyResponse>>> = MutableLiveData()

    //文章列表
    var publicData : MutableLiveData<ListDataUiState<AriticleResponse>> = MutableLiveData()

    //获取公众号标题
    fun getPublicTitleData(){
        request({ apiService.getPublicTitle()},titleData )
    }

    //获取公众号文章列表
    fun getPublicData(isRefresh : Boolean, id: Int){
        if (isRefresh){
            pageNo = 1
        }
        request({ apiService.getPublicData(pageNo, id)},{
            //数据请求成功
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
            publicData.value = listDataUiState
        },{
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errorMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<AriticleResponse>()
                )
            publicData.value = listDataUiState
        })

    }
}