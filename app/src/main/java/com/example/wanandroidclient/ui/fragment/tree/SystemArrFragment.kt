package com.example.wanandroidclient.ui.fragment.tree

import android.os.Bundle
import com.example.wanandroidclient.R
import com.example.wanandroidclient.app.base.BaseFragment
import com.example.wanandroidclient.databinding.FragmentSystemBinding
import com.example.wanandroidclient.databinding.FragmentSystemBindingImpl
import com.example.wanandroidclient.viewmodel.state.TreeViewModel

/**
 * 作者　: mmh
 * 时间　: 2021/10/29
 * 描述　: 这个是在体系的条目中点进去后的父Fragment
 */
class SystemArrFragment :BaseFragment<TreeViewModel, FragmentSystemBinding>(){
    override fun layoutId(): Int {
        return R.layout.fragment_system
    }

    override fun initView(savedInstanceState: Bundle?) {

    }
}