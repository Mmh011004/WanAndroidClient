package com.example.wanandroidclient.ui.fragment.project

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
import com.example.wanandroidclient.viewmodel.state.ProjectViewModel
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*

/**
 * 作者　: mmh
 * 时间　: 2021/10/25
 * 描述　:
 */
class ProjectChildFragment: BaseFragment<ProjectViewModel, IncludeListBinding>() {
    //适配器
    private val articleAdapter: AriticleAdapter by lazy { AriticleAdapter(arrayListOf()) }

    //页面管理者
    private lateinit var loadsir :LoadService<Any>

    //recyclerview的底部加载view 因为在首页要动态改变他的颜色，所以加了他这个字段
    private lateinit var footView: DefineLoadMoreView

    //是否是最新项目
     private var isNew = false

    //该项目对应的id，就是上面分类的接口
    private var cid = 0

    //请求的ViewModel
    private val requestProjectViewModel: RequestProjectViewModel by viewModels()

    companion object{
        fun newInstance(cid:Int, isNew: Boolean): ProjectChildFragment{
            val args = Bundle()
            args.putInt("cid", cid)
            args.putBoolean("isNew", isNew)
            val fragment = ProjectChildFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun layoutId(): Int {
        return R.layout.include_list
    }

    override fun initView(savedInstanceState: Bundle?) {
        arguments?.let {
            isNew = it.getBoolean("isNew")
            cid = it.getInt("cid")
        }
        //状态页配置
        loadsir = loadServiceInit(swipeRefresh){
            //点击重试时触发的操作
            loadsir.showLoading()
            //获取文章数据
            requestProjectViewModel.getProjectData(true, cid, isNew)
        }
        //初始化recyclerview
        recyclerView.init(LinearLayoutManager(context), articleAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))

            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
                //触发加载更多时请求数据
                requestProjectViewModel.getProjectData(false, cid, isNew)
            })

            // TODO: 2021/10/25 FloatBtn
        }

        swipeRefresh.init {
            //触发刷新的监听的时候执行 请求数据
            requestProjectViewModel.getProjectData(true, cid, isNew)
        }

        articleAdapter.run {
            //TODO setCollectClick


            setOnItemClickListener { adapter, view, position ->
                nav().navigateAction(R.id.action_to_webFragment, Bundle().apply {
                    putParcelable("ariticleData", articleAdapter.data[position])
                })
            }
            addChildClickViewIds(R.id.item_home_author,R.id.item_project_author)
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.item_home_author, R.id.item_project_author -> {
                        nav().navigateAction(
                            R.id.action_mainFragment_to_lookInfoFragment,
                            Bundle().apply {
                                putInt("id", articleAdapter.data[position].userId)
                            })
                    }
                }
            }
        }

    }

    override fun lazyLoadData() {
        loadsir.showLoading()
        requestProjectViewModel.getProjectData(true, cid, isNew)
    }

    override fun createObserver() {
        requestProjectViewModel.projectDataState.observe(viewLifecycleOwner, Observer {
            //项目文章数据
            loadListData(it, articleAdapter, loadsir, recyclerView, swipeRefresh)
            })
        // TODO: 2021/10/25 关于收藏收藏

        appViewModel.run {
            // TODO: 2021/10/25 预留账户的改变

            //监听全局的主题颜色改变
            appColor.observeInFragment(this@ProjectChildFragment, Observer {
                setUiTheme(it, floatbtn, swipeRefresh, loadsir, footView)
            })
            //监听全局的列表动画改编
            appAnimation.observeInFragment(this@ProjectChildFragment, Observer {
                articleAdapter.setAdapterAnimation(it)
            })
        }


    }

}