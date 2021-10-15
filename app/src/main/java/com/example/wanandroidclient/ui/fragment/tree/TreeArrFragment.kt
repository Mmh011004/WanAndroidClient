package com.example.wanandroidclient.ui.fragment.tree

import android.os.Bundle
import com.example.wanandroidclient.R
import com.example.wanandroidclient.app.base.BaseFragment
import com.example.wanandroidclient.databinding.FragmentViewpagerBinding
import com.example.wanandroidclient.viewmodel.state.TreeViewModel

/**
 * 作者　: mmh
 * 时间　: 2021/10/14
 * 描述　:
 */
class TreeArrFragment : BaseFragment<TreeViewModel, FragmentViewpagerBinding>() {
    override fun layoutId(): Int = R.layout.fragment_viewpager

    override fun initView(savedInstanceState: Bundle?) {

    }
}