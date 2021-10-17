package com.example.wanandroidclient.ui.fragment.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wanandroidclient.R
import com.example.wanandroidclient.app.base.BaseFragment
import com.example.wanandroidclient.databinding.FragmentSearchBinding
import com.example.wanandroidclient.viewmodel.state.SearchViewModel


class SearchFragment : BaseFragment<SearchViewModel, FragmentSearchBinding>(){
    override fun layoutId(): Int = R.layout.fragment_search

    override fun initView(savedInstanceState: Bundle?) {

    }


}