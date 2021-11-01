package com.example.wanandroidclient.ui.fragment.tree

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.example.jetpackmvvm.ext.nav
import com.example.jetpackmvvm.ext.navigateAction
import com.example.wanandroidclient.R
import com.example.wanandroidclient.app.appViewModel
import com.example.wanandroidclient.app.base.BaseFragment
import com.example.wanandroidclient.app.ext.*
import com.example.wanandroidclient.app.weight.recyclerview.DefineLoadMoreView
import com.example.wanandroidclient.app.weight.recyclerview.SpaceItemDecoration
import com.example.wanandroidclient.databinding.IncludeListBinding
import com.example.wanandroidclient.ui.adapter.AriticleAdapter
import com.example.wanandroidclient.ui.adapter.NavigationAdapter
import com.example.wanandroidclient.viewmodel.request.RequestTreeViewModel
import com.example.wanandroidclient.viewmodel.state.TreeViewModel
import com.kingja.loadsir.core.LoadService
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*

/**
 * 作者　: mmh
 * 时间　: 2021/10/26
 * 描述　: 导航的fragment
 */
class NavigationFragment : BaseFragment<TreeViewModel, IncludeListBinding>(){

    //适配器
    private val navigationAdapter: NavigationAdapter by lazy { NavigationAdapter(arrayListOf()) }

    //页面管理者
    private lateinit var loadsir : LoadService<Any>

    //请求的ViewModel
    private val requestTreeViewModel: RequestTreeViewModel by viewModels()


    override fun layoutId(): Int {
        return R.layout.include_list
    }

    override fun initView(savedInstanceState: Bundle?) {
        //状态页配置
        loadsir = loadServiceInit(swipeRefresh){
            loadsir.showLoading()
            requestTreeViewModel.getNavigationData()
        }
        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(context), navigationAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            //FloatBtn的行为没有自定义
            // it.initFloatBtn(floatbtn)TODO
        }

        //初始化swipeLayout
        swipeRefresh.init {
            //刷新触发的监听
            requestTreeViewModel.getNavigationData()
        }

        navigationAdapter.run {
            //设置小条目的跳转
            setNavigationAction { item, view ->
                //跳转
                nav().navigateAction(R.id.action_to_webFragment,
                    Bundle().apply {
                        putParcelable("ariticleData", item)
                    }
                )
                Log.d(TAG, "initView: 执行小条目跳转")
            }
        }
    }

    override fun lazyLoadData() {
        loadsir.showLoading()
        requestTreeViewModel.getNavigationData()
    }

    override fun createObserver() {
        requestTreeViewModel.navigationDataState.observe(viewLifecycleOwner, Observer {
            swipeRefresh.isRefreshing = false
            if (it.isSuccess) {
                loadsir.showSuccess()
                navigationAdapter.setList(it.listData)
            } else{
                loadsir.showError(it.errorMessage)
            }
        })

        appViewModel.run {
            //监听全局的主题颜色改变
            appColor.observeInFragment(this@NavigationFragment, Observer {
                setUiTheme(it, floatbtn, swipeRefresh, loadsir)
            })
            //监听全局的列表动画改编
            appAnimation.observeInFragment(this@NavigationFragment, Observer {
                navigationAdapter.setAdapterAnimation(it)
            })
        }
    }
}