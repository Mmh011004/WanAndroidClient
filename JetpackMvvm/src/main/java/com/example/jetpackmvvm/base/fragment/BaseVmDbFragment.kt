package com.example.jetpackmvvm.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * 作者　: mmh
 * 时间　: 2021/10/3
 * 描述　:
 */
abstract class BaseVmDbFragment<VM : BaseViewModel, DB : ViewDataBinding>:BaseVmFragment<VM>() {

    /*
    * 创建ViewDataBinding*/
    lateinit var mDataBinding : DB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDataBinding = DataBindingUtil.inflate(inflater,layoutId(),container,false)
        mDataBinding.lifecycleOwner = this
        return mDataBinding.root
    }
}