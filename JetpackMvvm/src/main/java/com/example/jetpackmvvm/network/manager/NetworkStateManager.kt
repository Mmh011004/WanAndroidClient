package com.example.jetpackmvvm.network.manager

import com.example.jetpackmvvm.callback.livedata.event.EventLiveData

/**
 * 作者　: mmh
 * 时间　: 2021/10/1
 * 描述　: 网络变化管理者
 */
class NetworkStateManager private constructor(){
    val mNetworkStateCallback = EventLiveData<NetState>()

    companion object {
        val instance: NetworkStateManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkStateManager()
        }
    }

}