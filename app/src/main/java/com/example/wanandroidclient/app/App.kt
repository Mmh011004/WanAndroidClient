package com.example.wanandroidclient.app

import android.util.Log
import com.example.jetpackmvvm.base.BaseApp
import com.example.jetpackmvvm.ext.util.jetpackMvvmLog
import com.example.jetpackmvvm.ext.util.logd
import com.example.wanandroidclient.app.event.AppViewModel
import com.example.wanandroidclient.app.event.EventViewModel
import com.example.wanandroidclient.app.ext.getProcessName
import com.example.wanandroidclient.app.weight.loadCallback.EmptyCallback
import com.example.wanandroidclient.app.weight.loadCallback.ErrorCallback
import com.example.wanandroidclient.app.weight.loadCallback.LoadingCallback
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadSir
import com.tencent.bugly.Bugly
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.mmkv.BuildConfig
import com.tencent.mmkv.MMKV

/**
 * 作者　: mmh
 * 时间　: 2021/10/12
 * 描述　:
 */

//Application的全局ViewModel，存放一些关于账户信息等
val appViewModel : AppViewModel by lazy { App.appViewModelInstance}

//Application全局的ViewModel，用于发送全局通知操作,通知
val eventViewModel : EventViewModel by lazy { App.eventViewModelInstance }

const val TAG = "App By Myself"

class App : BaseApp(){

    companion object{
        lateinit var instance : App
        lateinit var eventViewModelInstance : EventViewModel
        lateinit var appViewModelInstance : AppViewModel
    }

    //调用时机： 在应用程序启动时，在创建任何活动、服务或接收器对象（不包括内容提供者）之前调用。
    override fun onCreate() {
        super.onCreate()
        //这个是微信的工具，代替sharedPreference，效率远高于代替sharedPreference
        //初始化
        //CacheUtil里面用到
        MMKV.initialize(this.filesDir.absolutePath + "/mmkv")
        instance = this
        eventViewModelInstance = getAppViewModelProvider().get(EventViewModel::class.java)

        appViewModelInstance = getAppViewModelProvider().get(AppViewModel::class.java)
        Log.d(TAG, "onCreate: appViewModelInstance is created")

        //界面加载管理 初始化
        LoadSir.beginBuilder()
            .addCallback(LoadingCallback())//加载
            .addCallback(ErrorCallback())//错误
            .addCallback(EmptyCallback())//空
            .setDefaultCallback(SuccessCallback::class.java)//设置默认加载状态页
            .commit()

        //初始化bugly，bugly是腾讯的一个崩溃统计
        val context = applicationContext
        // 获取当前包名
        val packageName = context.packageName
        // 获取当前进程名
        val processName = getProcessName(android.os.Process.myPid())
        // 设置是否为上报进程
        val strategy = CrashReport.UserStrategy(context)
        strategy.isUploadProcess = processName == null || processName == packageName
        // 初始化Bugly
        Bugly.init(context, if (BuildConfig.DEBUG) "xxx" else "a52f2b5ebb", BuildConfig.DEBUG)
        "".logd()
        jetpackMvvmLog = BuildConfig.DEBUG
    }


}