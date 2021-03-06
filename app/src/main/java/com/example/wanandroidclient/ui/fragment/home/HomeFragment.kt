package com.example.wanandroidclient.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.example.jetpackmvvm.ext.nav
import com.example.jetpackmvvm.ext.navigateAction
import com.example.jetpackmvvm.ext.util.logd
import com.example.jetpackmvvm.ext.view.parseState
import com.example.jetpackmvvm.util.LogUtils
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
const val TAG = "HomeFragment"
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    //?????????
    private val articleAdapter: AriticleAdapter by lazy { AriticleAdapter(arrayListOf(), true) }

    //???????????????
    private lateinit var loadSir :LoadService<Any>

    //????????????ViewModel
    private val requestHomeViewModel: RequestHomeViewModel by viewModels()

    //??????????????????
    private  lateinit var footView : DefineLoadMoreView

    //??????ViewModel TODO

    override fun layoutId(): Int {
        Log.d(TAG, "layoutId: ????????????")
        return R.layout.fragment_home
    }

    override fun initView(savedInstanceState: Bundle?) {
        loadSir = loadServiceInit(swipeRefresh) {
            //???????????????lambda??????????????????????????????loadServiceInit???callback???lambda??????

            //??????????????????????????????
            loadSir.showLoading()
            requestHomeViewModel.getHomeData(true)
            requestHomeViewModel.getBannerData()
        }
        //?????????toolbar
        toolbar.run {
            //???????????????????????????
            init("??????")
            inflateMenu(R.menu.home_menu)
            //??????????????????
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.home_search -> {
                        nav().navigateAction(R.id.action_mainFragment_to_searchFragment)
                    }
                }
                true
            }


        }
        //?????????recyclerView
        recyclerView.init(LinearLayoutManager(context), articleAdapter).let {
            //Log.d(TAG, "initView: $articleAdapter")
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f), false))
            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
                    //??????SwipeRecyclerView.LoadMoreListener??????
                    requestHomeViewModel.getHomeData(false)
                })
            //FloatBtn????????????????????????
            // it.initFloatBtn(floatbtn)TODO
        }

        //?????????SwipeRefreshLayout
        swipeRefresh.init {
            //?????????????????????????????????
            requestHomeViewModel.getHomeData(true)
        }


        articleAdapter.run {

            //TODO setCollectClick

            setOnItemClickListener { adapter, view, position ->
                nav().navigateAction(R.id.action_to_webFragment, Bundle().apply {
                    //Bundle???????????????????????????
                    putParcelable(
                        "ariticleData",
                        articleAdapter.data[position - this@HomeFragment.recyclerView.headerCount]
                    )
                })
                Log.d(TAG, "initView: ??????web")
            }
            //??????Item??????View???????????????
            addChildClickViewIds(R.id.item_home_author,R.id.item_project_author)
            //???????????????bug?????????????????????????????????  ???addChildClickViewIds?????????????????????View???????????????setOnItemChildClickListener
            //???????????????setOnItemClickListener
            setOnItemChildClickListener { adapter, view, position ->
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
                //Log.d(TAG, "initView: ${view.id}")
            }

        }

    }

    /*
    * ?????????
    * ??????app??????*/

    override fun lazyLoadData() {
        //????????????????????????
        loadSir.showLoading()
        //?????????????????????
        requestHomeViewModel.getBannerData()
        //????????????????????????
        requestHomeViewModel.getHomeData(true)
    }


    override fun createObserver() {
        requestHomeViewModel.run {
            LogUtils.debugInfo("HomeFragment","hhh")
            //???????????????????????????????????????
            homeDataState.observe(viewLifecycleOwner, Observer {
                loadListData(it, articleAdapter, loadSir, recyclerView, swipeRefresh)
                //Log.d(TAG, "createObserver: ${articleAdapter.data}")
            })
            //??????????????????????????????
            bannerData.observe(viewLifecycleOwner, Observer { resultState ->
                parseState(resultState,{ data ->
                    //???????????????????????????
                    //??????????????????recyclerView???headView?????????headCount??????0????????????????????????????????????
                    if (recyclerView.headerCount == 0){
                        val headView = LayoutInflater.from(context)
                            .inflate(R.layout.include_banner,null).apply {
                                findViewById<BannerViewPager<BannerResponse, HomeBannerViewHolder>>(R.id.banner_view).apply {
                                    adapter = HomeBannerAdapter()
                                    //???????????????????????????
                                    setLifecycleRegistry(lifecycle)
                                    setOnPageClickListener {
                                        nav().navigateAction(R.id.action_to_webFragment, Bundle().apply {
                                            putParcelable("bannerdata", data[it])
                                        })
                                    }

                                    create(data)
                                }
                        }
                        //??????????????????????????????
                        recyclerView.addHeaderView(headView)
                        recyclerView.scrollToPosition(0)
                    }

                })
            })
        }

        appViewModel.run {
            //?????????????????????????????? ?????????(??????)????????????????????????????????????????????????(????????????)???????????????????????????????????????
            //TODO


            //?????????????????????????????????
            appColor.observeInFragment(this@HomeFragment) {
                setUiTheme(it, toolbar, floatbtn, swipeRefresh, loadSir, footView)
            }
            //?????????????????????????????????
            appAnimation.observeInFragment(this@HomeFragment) {
                articleAdapter.setAdapterAnimation(it)
            }
        }

    }
}







