package com.example.wanandroidclient.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.example.jetpackmvvm.base.viewmodel.BaseViewModel
import com.example.jetpackmvvm.state.ResultState
import com.example.wanandroidclient.app.network.stateCallback.ListDataUiState
import com.example.wanandroidclient.data.model.bean.AriticleResponse
import com.example.wanandroidclient.data.model.bean.ClassifyResponse

/**
 * 作者　: mmh
 * 时间　: 2021/10/24
 * 描述　:
 */
class RequestProjectViewModel: BaseViewModel() {
    //页码，从1开始
    var pageNo = 1

    var titleData : MutableLiveData<ResultState<ArrayList<ClassifyResponse>>> = MutableLiveData()

    var projectDataState: MutableLiveData<ListDataUiState<AriticleResponse>> = MutableLiveData()

    //获取项目标题
    fun getProjectTitleData(){

    }

    //获取项目文章列表
    fun getProjectData(){

    }


}