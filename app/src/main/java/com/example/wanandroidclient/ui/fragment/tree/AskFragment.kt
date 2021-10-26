package com.example.wanandroidclient.ui.fragment.tree

import android.os.Bundle
import com.example.wanandroidclient.R
import com.example.wanandroidclient.app.base.BaseFragment
import com.example.wanandroidclient.databinding.IncludeListBinding
import com.example.wanandroidclient.viewmodel.state.TreeViewModel

/**
 * 作者　: mmh
 * 时间　: 2021/10/26
 * 描述　:
 */
class AskFragment : BaseFragment<TreeViewModel, IncludeListBinding>(){
    override fun layoutId(): Int {
        return R.layout.include_list
    }

    override fun initView(savedInstanceState: Bundle?) {
    }
}