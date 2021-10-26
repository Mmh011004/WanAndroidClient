package com.example.wanandroidclient.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.example.jetpackmvvm.base.viewmodel.BaseViewModel
import com.example.jetpackmvvm.ext.request
import com.example.wanandroidclient.app.network.apiService
import com.example.wanandroidclient.app.network.stateCallback.ListDataUiState
import com.example.wanandroidclient.data.model.bean.AriticleResponse
import com.example.wanandroidclient.data.model.bean.NavigationResponse
import com.example.wanandroidclient.data.model.bean.SystemResponse

/**
 * 作者　: mmh
 * 时间　: 2021/10/26
 * 描述　:
 */
class RequestTreeViewModel: BaseViewModel() {
    //页码，体系 广场的页码是从0开始的
    private var pageNo = 0

    //广场数据
    var plazaDataState: MutableLiveData<ListDataUiState<AriticleResponse>> = MutableLiveData()

    //每日一问
    var askDataState: MutableLiveData<ListDataUiState<AriticleResponse>> =MutableLiveData()

    //体系的子栏目的数据列表
    var systemChildDataState : MutableLiveData<ListDataUiState<AriticleResponse>> = MutableLiveData()

    //体系数据
    var systemDataState : MutableLiveData<ListDataUiState<SystemResponse>> = MutableLiveData()

    //导航数据
    var navigationDataState : MutableLiveData<ListDataUiState<NavigationResponse>> = MutableLiveData()


    /*
    * 获取广场数据*/
    fun getPlazaData(isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 0
        }
        request({ apiService.getSquareData(pageNo) }, {
            //请求成功
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
            plazaDataState.value = listDataUiState
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errorMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<AriticleResponse>()
                )
            plazaDataState.value = listDataUiState
        })
    }

}