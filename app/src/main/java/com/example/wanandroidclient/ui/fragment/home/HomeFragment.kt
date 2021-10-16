package com.example.wanandroidclient.ui.fragment.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wanandroidclient.R
import com.example.wanandroidclient.app.base.BaseFragment
import com.example.wanandroidclient.app.ext.loadServiceInit
import com.example.wanandroidclient.app.ext.showLoading
import com.example.wanandroidclient.databinding.FragmentHomeBinding
import com.example.wanandroidclient.viewmodel.state.HomeViewModel
import com.kingja.loadsir.core.LoadService
import kotlinx.android.synthetic.main.include_recyclerview.*


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    //界面管理者
    private lateinit var loadSir :LoadService<Any>

    override fun layoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initView(savedInstanceState: Bundle?) {
        loadSir = loadServiceInit(swipeRefresh){
            //这里是一个lambda函数，是关于方法loadServiceInit的callback的lambda函数
            //点击重试时触发的操作
            loadSir.showLoading()
        }

    }


}