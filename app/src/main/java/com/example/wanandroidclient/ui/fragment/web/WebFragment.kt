package com.example.wanandroidclient.ui.fragment.web

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wanandroidclient.R
import com.example.wanandroidclient.app.base.BaseFragment
import com.example.wanandroidclient.app.ext.init
import com.example.wanandroidclient.data.model.bean.AriticleResponse
import com.example.wanandroidclient.data.model.bean.BannerResponse
import com.example.wanandroidclient.databinding.FragmentWebBinding
import com.example.wanandroidclient.viewmodel.state.WebViewModel
import com.just.agentweb.AgentWeb
import kotlinx.android.synthetic.main.include_toolbar.*


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class WebFragment : BaseFragment<WebViewModel, FragmentWebBinding>() {
    /*
    * AgentWeb 是一个基于的 Android WebView ，极度容易使用以及功能强大的库，
    * 提供了 Android WebView 一系列的问题解决方案 ，并且轻量和极度灵活，*/
    private var mAgentWeb : AgentWeb? = null

    private var preWeb: AgentWeb.PreAgentWeb? = null

    override fun layoutId(): Int = R.layout.fragment_web

    override fun initView(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        //返回实例化Fragment时提供的参数（如果有）。(百度翻译的这个Api。。。)
        arguments?.run {
            //点击文章进来的
            getParcelable<AriticleResponse>("ariticleData")?.let {
                mViewModel.ariticleId = it.id
                mViewModel.showTitle = it.title
                mViewModel.collect = it.collect
                mViewModel.url = it.link
                //还没有去写收藏
                //TODO mViewModel.collectType = CollectType.Ariticle.type
            }

            //从轮播图进来
            getParcelable<BannerResponse>("bannerdata")?.let {
                mViewModel.ariticleId = it.id
                mViewModel.showTitle = it.title
                //轮播图看不出是否收藏，虽然现在还没有写关于收藏的代码，
                // 是因为需要写登录和注册页面，打算最后写，用callback去调用
                mViewModel.collect = false
                mViewModel.url = it.url
                // TODO mViewModel.collectType = CollectType.Url.type
            }

            //从收藏文章列表点进来的
            //TODO

            //从收藏网址列表进来
            //TODO

        }

        //初始化toolbar
        toolbar.run {
            //设置menu的关键代码
            mActivity.setSupportActionBar(this)
            // TODO: 2021/10/23 初始化有返回键的toolbar 
        }
    }
}




















