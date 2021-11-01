package com.example.wanandroidclient.ui.fragment.tree

import android.os.Bundle
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
import com.example.wanandroidclient.viewmodel.request.RequestProjectViewModel
import com.example.wanandroidclient.viewmodel.request.RequestTreeViewModel
import com.example.wanandroidclient.viewmodel.state.TreeViewModel
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*

/**
 * 作者　: mmh
 * 时间　: 2021/10/31
 * 描述　:
 */
class SystemChildFragment : BaseFragment<TreeViewModel, IncludeListBinding>() {

    //适配器
    private val articleAdapter: AriticleAdapter by lazy { AriticleAdapter(arrayListOf()) }

    //页面管理者
    private lateinit var loadsir: LoadService<Any>

    //recyclerview的底部加载view 因为在首页要动态改变他的颜色，所以加了他这个字段
    private lateinit var footView: DefineLoadMoreView

    private var cid = -1

    //请求的ViewModel
    private val requestTreeViewModel: RequestTreeViewModel by viewModels()

    companion object {
        fun newInstance(cid: Int): SystemChildFragment {
            return SystemChildFragment().apply {
                arguments = Bundle().apply {
                    putInt("cid", cid)
                }
            }
        }
    }

    override fun layoutId(): Int {
        return R.layout.include_list
    }

    override fun initView(savedInstanceState: Bundle?) {
        arguments?.let {
            cid = it.getInt("cid")
        }
        //状态页配置
        loadsir = loadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            loadsir.showLoading()
            //获取文章数据
            requestTreeViewModel.getSystemChildData(true, cid)
        }

        //初始化recyclerview
        recyclerView.init(LinearLayoutManager(context), articleAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))

            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
                //触发加载更多时请求数据
                requestTreeViewModel.getSystemChildData(false, cid)
            })

            // TODO: 2021/10/25 FloatBtn
        }

        //swipeLayout初始化
        swipeRefresh.init {
            //刷新触发监听
            requestTreeViewModel.getSystemChildData(true, cid)
        }

        articleAdapter.run {
            //TODO setCollectClick

            setOnItemClickListener { adapter, view, position ->
                nav().navigateAction(R.id.action_to_webFragment, Bundle().apply {
                    putParcelable("ariticleData", articleAdapter.data[position])
                })
            }
            addChildClickViewIds(R.id.item_home_author, R.id.item_project_author)
            setOnItemChildClickListener { adapter, view, position ->
                setOnItemChildClickListener { adapter, view, position ->
                    when (view.id) {
                        R.id.item_home_author, R.id.item_project_author -> {
                            nav().navigateAction(
                                R.id.action_systemArrFragment_to_lookInfoFragment,
                                Bundle().apply {
                                    putInt("id", articleAdapter.data[position].userId)
                                })
                        }
                    }
                }
            }
        }
    }

    override fun lazyLoadData() {
        loadsir.showLoading()
        requestTreeViewModel.getSystemChildData(true, cid)
    }

    override fun createObserver() {
        requestTreeViewModel.systemChildDataState.observe(viewLifecycleOwner, Observer {
            loadListData(it, articleAdapter, loadsir, recyclerView, swipeRefresh)
        })
        // TODO: 2021/10/25 关于收藏收藏

        appViewModel.run {
            // TODO: 2021/10/25 预留账户的改变

            //监听全局的主题颜色改变
            appColor.observeInFragment(this@SystemChildFragment, Observer {
                setUiTheme(it, floatbtn, swipeRefresh, loadsir, footView)
            })
            //监听全局的列表动画改编
            appAnimation.observeInFragment(this@SystemChildFragment, Observer {
                articleAdapter.setAdapterAnimation(it)
            })
        }
    }
}