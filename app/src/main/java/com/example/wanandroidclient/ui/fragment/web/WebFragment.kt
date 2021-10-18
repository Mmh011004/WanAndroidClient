package com.example.wanandroidclient.ui.fragment.web

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wanandroidclient.R
import com.example.wanandroidclient.app.base.BaseFragment
import com.example.wanandroidclient.databinding.FragmentWebBinding
import com.example.wanandroidclient.viewmodel.state.WebViewModel


class WebFragment : BaseFragment<WebViewModel,FragmentWebBinding>() {
    override fun layoutId(): Int = R.layout.fragment_web

    override fun initView(savedInstanceState: Bundle?) {

    }


}