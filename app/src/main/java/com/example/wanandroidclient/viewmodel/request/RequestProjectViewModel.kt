package com.example.wanandroidclient.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.example.jetpackmvvm.base.viewmodel.BaseViewModel
import com.example.jetpackmvvm.ext.request
import com.example.jetpackmvvm.state.ResultState
import com.example.wanandroidclient.app.network.apiService
import com.example.wanandroidclient.app.network.stateCallback.ListDataUiState
import com.example.wanandroidclient.data.model.bean.AriticleResponse
import com.example.wanandroidclient.data.model.bean.ClassifyResponse
import com.example.wanandroidclient.data.repository.request.HttpRequestCoroutine

/**
 * 作者　: mmh
 * 时间　: 2021/10/24
 * 描述　:
 */
class RequestProjectViewModel: BaseViewModel() {
    //页码，从1开始
    var pageNo = 1

    //分类的数据
    var titleData : MutableLiveData<ResultState<ArrayList<ClassifyResponse>>> = MutableLiveData()

    //文章列表数据
    var projectDataState: MutableLiveData<ListDataUiState<AriticleResponse>> = MutableLiveData()

    //获取项目标题
    fun getProjectTitleData(){
        request({ apiService.getProjectTitle()}, titleData)
    }

    //获取项目文章列表
    //cid 就是分类的id，上面项目分类接口
    fun getProjectData(isRefresh: Boolean, cid: Int, isNew: Boolean = false){
        if (isRefresh){
            pageNo = if (isNew) 0 else 1
        }
        request({ HttpRequestCoroutine.getProjectData(pageNo,cid,isNew)},{
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
            projectDataState.value = listDataUiState

        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errorMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<AriticleResponse>()
                )
            projectDataState.value = listDataUiState
        })

    }


}