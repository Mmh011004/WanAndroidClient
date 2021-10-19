package com.example.wanandroidclient.ui.fragment.lookinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wanandroidclient.R
import com.example.wanandroidclient.app.base.BaseFragment
import com.example.wanandroidclient.databinding.FragmentLookInfoBinding
import com.example.wanandroidclient.viewmodel.state.LookInfoViewModel


class LookInfoFragment : BaseFragment<LookInfoViewModel,FragmentLookInfoBinding>() {
    override fun layoutId(): Int  = R.layout.fragment_look_info

    override fun initView(savedInstanceState: Bundle?) {

    }


}