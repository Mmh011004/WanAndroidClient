package com.example.wanandroidclient.ui.fragment.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jetpackmvvm.ext.nav
import com.example.jetpackmvvm.ext.navigateAction
import com.example.wanandroidclient.R
import com.example.wanandroidclient.app.base.BaseFragment
import com.example.wanandroidclient.app.ext.init
import com.example.wanandroidclient.app.ext.loadServiceInit
import com.example.wanandroidclient.app.ext.showLoading
import com.example.wanandroidclient.databinding.FragmentHomeBinding
import com.example.wanandroidclient.ui.adapter.AriticleAdapter
import com.example.wanandroidclient.viewmodel.request.RequestHomeViewModel
import com.example.wanandroidclient.viewmodel.state.HomeViewModel
import com.kingja.loadsir.core.LoadService
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.android.synthetic.main.include_toolbar.*


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    //适配器
    private val articleAdapter: AriticleAdapter by lazy { AriticleAdapter(arrayListOf(), true) }

    //界面管理者
    private lateinit var loadSir :LoadService<Any>

    //请求数据ViewModel
    private val requestHomeViewModel: RequestHomeViewModel by viewModels()

    override fun layoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initView(savedInstanceState: Bundle?) {
        loadSir = loadServiceInit(swipeRefresh){
            //这里是一个lambda高阶函数，是关于方法loadServiceInit的callback的lambda函数

            //点击重试时触发的操作
            loadSir.showLoading()
            requestHomeViewModel.getHomeData(true)
            requestHomeViewModel.getBannerData()
        }
        //初始化toolbar
        toolbar.run {
            //只设置标题的初始化
            init("首页")
            inflateMenu(R.menu.home_menu)
            //点击事件待做
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.home_search -> {
                        nav().navigateAction(R.id.action_mainFragment_to_searchFragment)
                    }
                }
                true
            }


        }
        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(context),articleAdapter).let {

        }

    }


}






