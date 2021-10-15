package com.example.wanandroidclient.app.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.example.jetpackmvvm.base.activity.BaseVmDbActivity
import com.example.jetpackmvvm.base.viewmodel.BaseViewModel
import com.example.wanandroidclient.app.ext.dismissLoadingExt
import com.example.wanandroidclient.app.ext.showLoadingExt

/**
 * 作者　: mmh
 * 时间　: 2021/10/13
 * 描述　: activity基类
 */
abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmDbActivity<VM, DB>() {
    abstract override fun layoutId(): Int

    abstract override fun initView(savedInstanceState: Bundle?)

    /**
     * 打开等待框
     */
    override fun showLoading(message: String) {
        showLoadingExt(message)
    }

    /**
     * 关闭等待框
     */
    override fun dismissLoading() {
        dismissLoadingExt()
    }

    //创建观察者
    abstract override fun createObserver()




}