package com.example.wanandroidclient.ui.fragment.web

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import com.example.jetpackmvvm.ext.nav
import com.example.wanandroidclient.R
import com.example.wanandroidclient.app.base.BaseFragment
import com.example.wanandroidclient.app.ext.hideSoftKeyboard
import com.example.wanandroidclient.app.ext.initClose
import com.example.wanandroidclient.data.model.bean.AriticleResponse
import com.example.wanandroidclient.data.model.bean.BannerResponse
import com.example.wanandroidclient.databinding.FragmentWebBinding
import com.example.wanandroidclient.viewmodel.state.WebViewModel
import com.just.agentweb.AgentWeb
import kotlinx.android.synthetic.main.fragment_web.*
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
            //初始化有返回键的toolbar
            initClose(mViewModel.showTitle){
                hideSoftKeyboard(activity)
                mAgentWeb?.let { web ->
                    if (web.webCreator.webView.canGoBack()){
                        web.webCreator.webView.goBack()
                    }else{
                        nav().navigateUp()
                    }
                }
            }
        }

        //这是AgentWeb的基本用法，具体查看github
        preWeb =AgentWeb.with(this)
            .setAgentWebParent(webcontent, LinearLayout.LayoutParams(-1,-1))
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()
    }


    override fun lazyLoadData() {
        //加载网页
        mAgentWeb = preWeb?.go(mViewModel.url)
        //处理回退事件
        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    mAgentWeb?.let { web ->
                        if (web.webCreator.webView.canGoBack()) {
                            web.webCreator.webView.goBack()
                        } else {
                            nav().navigateUp()
                        }
                    }
                }
            })
    }


    override fun createObserver() {
        // TODO: 2021/10/24 这里是关于收藏时触发的效果
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.web_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    //每一次显示菜单的时候都会调用次方法
    //实现动态显示
    //TODO
   /* override fun onPrepareOptionsMenu(menu: Menu) {

    }*/



    // TODO: 2021/10/24 onOptionsItemSelected
    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {

    }*/




    override fun onPause() {
        mAgentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAgentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        mAgentWeb?.webLifeCycle?.onDestroy()
        mActivity.setSupportActionBar(null)
        super.onDestroy()
    }
}




















