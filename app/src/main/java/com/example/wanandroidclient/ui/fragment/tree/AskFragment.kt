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
import com.example.wanandroidclient.viewmodel.request.RequestTreeViewModel
import com.example.wanandroidclient.viewmodel.state.TreeViewModel
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*

/**
 * 作者　: mmh
 * 时间　: 2021/10/26
 * 描述　:
 */
class AskFragment : BaseFragment<TreeViewModel, IncludeListBinding>() {

    //适配器
    private val articleAdapter: AriticleAdapter by lazy { AriticleAdapter(arrayListOf()) }

    //页面管理者
    private lateinit var loadsir: LoadService<Any>

    //recyclerview的底部加载view 因为在首页要动态改变他的颜色，所以加了他这个字段
    private lateinit var footView: DefineLoadMoreView

    //请求的ViewModel
    private val requestTreeViewModel: RequestTreeViewModel by viewModels()

    override fun layoutId(): Int {
        return R.layout.include_list
    }

    override fun initView(savedInstanceState: Bundle?) {
        //状态页配置
        loadsir = loadServiceInit(swipeRefresh) {
            //点击刷新触发
            loadsir.showLoading()
            //获取广场数据
            requestTreeViewModel.getAskData(true)
        }
        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(context), articleAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f), false))
            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
                //实现SwipeRecyclerView.LoadMoreListener接口
                requestTreeViewModel.getAskData(false)
            })
            //FloatBtn的行为没有自定义
            // it.initFloatBtn(floatbtn)TODO
        }

        //初始化swipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            requestTreeViewModel.getAskData(true)
        }

        articleAdapter.run {

            //TODO setCollectClick

            setOnItemClickListener { adapter, view, position ->
                nav().navigateAction(R.id.action_to_webFragment, Bundle().apply {
                    //Bundle传递对象的方法之一
                    putParcelable(
                        "ariticleData",
                        articleAdapter.data[position]
                    )
                })
                Log.d(com.example.wanandroidclient.ui.fragment.home.TAG, "initView: 跳转web")
            }
            //设置Item的子View的点击事件
            addChildClickViewIds(R.id.item_home_author, R.id.item_project_author)
            //在此处出现bug：点击事件无响应，解决  在addChildClickViewIds，应该是设置子View的监听器：setOnItemChildClickListener
            //而不是使用setOnItemClickListener
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.item_home_author, R.id.item_project_author -> {
                        nav().navigateAction(
                            R.id.action_mainFragment_to_lookInfoFragment,
                            Bundle().apply {
                                putInt(
                                    "id",
                                    articleAdapter.data[position - this@AskFragment.recyclerView.headerCount].userId
                                )
                            }
                        )
                    }
                }
            }
            Log.d(TAG, "initView: ${this@AskFragment.recyclerView.headerCount}")
            //headerCount = 0,可能position - this@PlazaFragment.recyclerView.headerCount这一句是一个保险
        }
    }

    override fun lazyLoadData() {
        loadsir.showLoading()
        requestTreeViewModel.getAskData(true)
    }

    override fun createObserver() {
        requestTreeViewModel.askDataState.observe(viewLifecycleOwner, Observer {
            //加载数据的方法在这里
            loadListData(it, articleAdapter, loadsir, recyclerView, swipeRefresh)
        })

        // TODO: 2021/10/28 关于收藏收藏

        //监听全局修改主题颜色
        appViewModel.run {
            appColor.observeInFragment(this@AskFragment, Observer {
                setUiTheme(it, floatbtn, swipeRefresh, loadsir, footView)
            })

            //监听全局的列表动画改编
            appAnimation.observeInFragment(this@AskFragment, Observer {
                articleAdapter.setAdapterAnimation(it)
            })
        }
    }
}

