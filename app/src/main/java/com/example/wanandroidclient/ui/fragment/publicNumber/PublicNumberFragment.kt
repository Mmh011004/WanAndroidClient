package com.example.wanandroidclient.ui.fragment.publicNumber

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wanandroidclient.R
import com.example.wanandroidclient.app.base.BaseFragment
import com.example.wanandroidclient.databinding.FragmentViewpagerBinding
import com.example.wanandroidclient.viewmodel.request.RequestPublicNumberViewModel
import com.example.wanandroidclient.viewmodel.state.TreeViewModel



class PublicNumberFragment : BaseFragment<RequestPublicNumberViewModel, FragmentViewpagerBinding>(){
    override fun layoutId(): Int {
        return R.layout.fragment_viewpager
    }

    override fun initView(savedInstanceState: Bundle?) {
    }


}