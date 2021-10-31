package com.example.wanandroidclient.ui.fragment.tree

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.example.jetpackmvvm.ext.nav
import com.example.jetpackmvvm.ext.navigateAction
import com.example.wanandroidclient.R
import com.example.wanandroidclient.app.appViewModel
import com.example.wanandroidclient.app.base.BaseFragment
import com.example.wanandroidclient.app.ext.*
import com.example.wanandroidclient.app.weight.recyclerview.SpaceItemDecoration
import com.example.wanandroidclient.data.model.bean.SystemResponse
import com.example.wanandroidclient.databinding.IncludeListBinding
import com.example.wanandroidclient.ui.adapter.SystemAdapter
import com.example.wanandroidclient.viewmodel.request.RequestTreeViewModel
import com.example.wanandroidclient.viewmodel.state.TreeViewModel
import com.kingja.loadsir.core.LoadService
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*


class SystemFragment : BaseFragment<TreeViewModel, IncludeListBinding>() {


    //界面状态管理者
    private lateinit var loadsir : LoadService<Any>

    //适配器
    private val systemAdapter: SystemAdapter by lazy { SystemAdapter(arrayListOf()) }

    //viewModel
    private val requestTreeViewModel : RequestTreeViewModel by viewModels()
    override fun layoutId(): Int {
        return R.layout.include_list
    }

    override fun initView(savedInstanceState: Bundle?) {
        //状态页配置
        loadsir = loadServiceInit(swipeRefresh){
            loadsir.showLoading()
            requestTreeViewModel.getSystemData()
        }

        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(context), systemAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            //FloatBtn的行为没有自定义
            // it.initFloatBtn(floatbtn)TODO
        }

        //初始化SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新时监听 数据请求
            requestTreeViewModel.getSystemData()
        }

        //设置点击事件
        systemAdapter.run {
            //点击大条目的跳转
            setOnItemClickListener { _, view, position ->
                if (systemAdapter.data[position].children.isNotEmpty()){
                    nav().navigateAction(R.id.action_mainFragment_to_systemArrFragment,
                        Bundle().apply {
                            putParcelable("data", systemAdapter.data[position])
                        }
                    )
                }
            }
            //点击大条目中的小条目的跳转
            setChildClick { item: SystemResponse, view, position ->
                nav().navigateAction(R.id.action_mainFragment_to_systemArrFragment,
                    Bundle().apply {
                        putParcelable("data",item)
                        putInt("index", position)
                    }
                )
            }

        }
    }
    override fun lazyLoadData() {
        //设置界面 加载中
        loadsir.showLoading()
        requestTreeViewModel.getSystemData()
    }

    override fun createObserver() {
        requestTreeViewModel.systemDataState.observe(viewLifecycleOwner, Observer {
            swipeRefresh.isRefreshing = false
            if (it.isSuccess){
                loadsir.showSuccess()
                systemAdapter.setList(it.listData)
            } else{
                loadsir.showError(it.errorMessage)
            }
        })

        appViewModel.run {
            //监听全局的主题颜色改变
            appColor.observeInFragment(this@SystemFragment, Observer {
                setUiTheme(it, floatbtn, swipeRefresh, loadsir)
            })
            //监听全局的列表动画改编
            appAnimation.observeInFragment(this@SystemFragment, Observer {
                systemAdapter.setAdapterAnimation(it)
            })
        }
    }

}