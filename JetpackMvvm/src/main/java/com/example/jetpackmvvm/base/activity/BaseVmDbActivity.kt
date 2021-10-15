package com.example.jetpackmvvm.base.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * 作者　: mmh
 * 时间　: 2021/10/1
 * 描述　: 包含ViewModel和DataBinding的ViewModelActivity基类，需要将VIew Model和DataBinding注入进来
 *        这个类需要有DataBinding来继承这个类
 */
abstract class BaseVmDbActivity<VM : BaseViewModel, DB : ViewDataBinding>:BaseVmActivity<VM>() {
    lateinit var mDataBinding: DB

    override fun onCreate(savedInstanceState: Bundle?) {
        userDataBinding(true)
        super.onCreate(savedInstanceState)
    }


    /*
    * 创建DaTaBinding
    * */

    override fun initDataBind() {
        mDataBinding = DataBindingUtil.setContentView(this,layoutId())
        mDataBinding.lifecycleOwner = this
    }
}