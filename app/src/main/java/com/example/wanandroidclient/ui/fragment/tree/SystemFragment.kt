package com.example.wanandroidclient.ui.fragment.tree

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wanandroidclient.R
import com.example.wanandroidclient.app.base.BaseFragment
import com.example.wanandroidclient.databinding.IncludeListBinding
import com.example.wanandroidclient.viewmodel.state.TreeViewModel


class SystemFragment : BaseFragment<TreeViewModel, IncludeListBinding>() {
    override fun layoutId(): Int {
        return R.layout.include_list
    }

    override fun initView(savedInstanceState: Bundle?) {
    }


}