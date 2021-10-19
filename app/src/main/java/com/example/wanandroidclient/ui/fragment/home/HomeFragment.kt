package com.example.wanandroidclient.ui.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.example.jetpackmvvm.ext.nav
import com.example.jetpackmvvm.ext.navigateAction
import com.example.jetpackmvvm.ext.view.parseState
import com.example.wanandroidclient.R
import com.example.wanandroidclient.app.appViewModel
import com.example.wanandroidclient.app.base.BaseFragment
import com.example.wanandroidclient.app.ext.*
import com.example.wanandroidclient.app.weight.banner.HomeBannerAdapter
import com.example.wanandroidclient.app.weight.banner.HomeBannerViewHolder
import com.example.wanandroidclient.app.weight.recyclerview.DefineLoadMoreView
import com.example.wanandroidclient.app.weight.recyclerview.SpaceItemDecoration
import com.example.wanandroidclient.data.model.bean.BannerResponse
import com.example.wanandroidclient.databinding.FragmentHomeBinding
import com.example.wanandroidclient.ui.adapter.AriticleAdapter
import com.example.wanandroidclient.viewmodel.request.RequestHomeViewModel
import com.example.wanandroidclient.viewmodel.state.HomeViewModel
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import com.zhpan.bannerview.BannerViewPager
import kotlinx.android.synthetic.main.include_list.*
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

    //加载更多视图
    private  lateinit var footView : DefineLoadMoreView

    override fun layoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initView(savedInstanceState: Bundle?) {
        loadSir = loadServiceInit(swipeRefresh) {
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
        recyclerView.init(LinearLayoutManager(context), articleAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f), false))
            footView = it.initFooter(//实现SwipeRecyclerView.LoadMoreListener接口
                SwipeRecyclerView.LoadMoreListener {
                    requestHomeViewModel.getHomeData(false)
                })
            //FloatBtn的行为没有自定义
            // it.initFloatBtn(floatbtn)
        }

        //初始化SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            requestHomeViewModel.getHomeData(true)
        }
        articleAdapter.run {


            setOnItemClickListener { adapter, view, position ->
                nav().navigateAction(R.id.action_to_webFragment, Bundle().apply {
                    //Intent传递对象的方法之一
                    putParcelable(
                        "ariticleData",
                        articleAdapter.data[position - this@HomeFragment.recyclerView.headerCount]
                    )
                })
            }

            //设置Item的子View的点击事件
            addChildClickViewIds(R.id.item_home_author,R.id.item_project_author)
            setOnItemClickListener { adapter, view, position ->
                when(view.id){
                    R.id.item_home_author,R.id.item_project_author -> {
                        nav().navigateAction(
                            R.id.action_mainFragment_to_lookInfoFragment,
                            Bundle().apply {
                                putInt(
                                    "id",
                                    articleAdapter.data[position - this@HomeFragment.recyclerView.headerCount].userId
                                )
                            }
                        )
                    }
                }
            }

        }

    }

    /*
    * 懒加载
    * 提高app性能*/

    override fun lazyLoadData() {
        //设置界面，加载中
        loadSir.showLoading()
        //请求轮播图数据
        requestHomeViewModel.getBannerData()
        //请求文章列表数据
        requestHomeViewModel.getHomeData(true)
    }


    override fun createObserver() {
        requestHomeViewModel.run {
            //监听首页文章列表的数据更新
            homeDataState.observe(viewLifecycleOwner, Observer {
                loadListData(it, articleAdapter, loadSir, recyclerView, swipeRefresh)
            })
            //监听轮播图的数据更新
            bannerData.observe(viewLifecycleOwner, Observer { resultState ->
                parseState(resultState,{ data ->
                    //轮播图数据请求成功
                    //添加轮播图到recyclerView的headView，如歌headCount等于0的话，就说明没有加入头部
                    if (recyclerView.headerCount == 0){
                        val headView = LayoutInflater.from(context)
                            .inflate(R.layout.include_banner,null).apply {
                                findViewById<BannerViewPager<BannerResponse, HomeBannerViewHolder>>(R.id.banner_view).apply {
                                    adapter = HomeBannerAdapter()
                                    //设置生命周期注册表
                                    setLifecycleRegistry(lifecycle)
                                    setOnPageClickListener {
                                        nav().navigateAction(R.id.action_to_webFragment, Bundle().apply {
                                            putParcelable("bannerdata", data[it])
                                        })
                                    }

                                    create(data)
                                }
                        }
                        //这里开始正式加入头部
                        recyclerView.addHeaderView(headView)
                        recyclerView.scrollToPosition(0)
                    }

                })
            })
        }

        appViewModel.run {
            //监听账户信息是否改变 有值时(登录)将相关的数据设置为已收藏，为空时(退出登录)，将已收藏的数据变为未收藏
            //TODO


            //监听全局的主题颜色改变
            appColor.observeInFragment(this@HomeFragment) {
                setUiTheme(it, toolbar, floatbtn, swipeRefresh, loadSir, footView)
            }
            //监听全局的列表动画改编
            appAnimation.observeInFragment(this@HomeFragment) {
                articleAdapter.setAdapterAnimation(it)
            }
        }

    }
}







