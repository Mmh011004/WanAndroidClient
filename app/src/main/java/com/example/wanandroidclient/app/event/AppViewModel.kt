package com.example.wanandroidclient.app.event

import com.example.jetpackmvvm.base.appContext
import com.example.jetpackmvvm.base.viewmodel.BaseViewModel
import com.example.jetpackmvvm.callback.livedata.event.EventLiveData
import com.example.wanandroidclient.app.util.CacheUtil
import com.example.wanandroidclient.app.util.SettingUtil
import com.example.wanandroidclient.data.model.bean.UserInfo
import com.kunminx.architecture.ui.callback.UnPeekLiveData

/**
 * 作者　: mmh
 * 时间　: 2021/10/12
 * 描述　: application级别的ViewModel，当数据改变时，所有监听有关数据的地方都会收到回调callback，
 * 存放一些公共数据，比如账户信息等
 */
class AppViewModel : BaseViewModel(){

    //账户信息，允许传入空值
    var userInfo = UnPeekLiveData.Builder<UserInfo>().setAllowNullValue(true).create()

    //App主题颜色 中大型项目不推荐以这种方式改变主题颜色，比较繁琐耦合，且容易有遗漏某些控件没有设置主题色
    var appColor = EventLiveData<Int>()

    //动画
    var appAnimation = EventLiveData<Int>()

    //kotlin提供的init代码块可以用来初始化属性，优先级是代码顺序来的，具体百度。
    //有点类似Java的静态代码块，类加载的时候只执行一次，又有点像companion object
    init {
        //默认值保存的账户信息，没有登陆过则为null
        userInfo.value = CacheUtil.getUser()
        //默认值颜色
        appColor.value = SettingUtil.getColor(appContext)
        //初始化列表动画
        appAnimation.value = SettingUtil.getListMode()

    }
}