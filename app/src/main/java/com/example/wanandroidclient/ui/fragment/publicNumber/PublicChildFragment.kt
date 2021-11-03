package com.example.wanandroidclient.ui.fragment.publicNumber

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
import com.example.wanandroidclient.viewmodel.request.RequestPublicNumberViewModel
import com.example.wanandroidclient.viewmodel.state.PublicNumberViewModel
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*

/**
 * 作者　: mmh
 * 时间　: 2021/11/2
 * 描述　:
 */
class PublicChildFragment:BaseFragment<PublicNumberViewModel, IncludeListBinding>() {
    
    val TAG = "PublicChildFragment"

    //适配器
    private val articleAdapter : AriticleAdapter by lazy { AriticleAdapter(arrayListOf()) }

    //状态页配置
    private lateinit var loadsir: LoadService<Any>

    //recyclerview的底部加载view 因为在首页要动态改变他的颜色，所以加了他这个字段
    private lateinit var footView: DefineLoadMoreView

    //该项目对应的id
    private var cid = 0

    //请求viewmodel
    private val requestPublicNumberViewModel: RequestPublicNumberViewModel by viewModels()

    companion object{
        fun newInstance(cid: Int): PublicChildFragment{
            val args = Bundle()
            args.putInt("cid", cid)
            val fragment = PublicChildFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun layoutId(): Int {
        return R.layout.include_list
    }

    override fun initView(savedInstanceState: Bundle?) {
        arguments?.let {
            //这里只能用it，之前使用的Bundle相当于是一个新的，里面是没有cid的值的
            cid = it.getInt("cid")
            Log.d(TAG, "initView: ${cid}")
        }
        loadsir = loadServiceInit(swipeRefresh) {
            //点击重试触发的操作
            loadsir.showLoading()
            requestPublicNumberViewModel.getPublicData(true, cid)
        }

        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(context),articleAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))

            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
                requestPublicNumberViewModel.getPublicData(false, cid)
            })
            // TODO: 2021/11/2 FloatBtn
        }

        //初始化swipeLayout
        swipeRefresh.init {
            //刷新触发
            requestPublicNumberViewModel.getPublicData(true, cid)
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
        requestPublicNumberViewModel.getPublicData(true, cid)
    }

    override fun createObserver() {
        requestPublicNumberViewModel.publicData.observe(viewLifecycleOwner, Observer {
            loadListData(it, articleAdapter, loadsir, recyclerView, swipeRefresh)
        })

        // TODO: 2021/10/25 关于收藏收藏

        appViewModel.run {
            // TODO: 2021/10/25 预留账户的改变

            //监听全局的主题颜色改变
            appColor.observeInFragment(this@PublicChildFragment, Observer {
                setUiTheme(it, floatbtn, swipeRefresh, loadsir, footView)
            })
            //监听全局的列表动画改编
            appAnimation.observeInFragment(this@PublicChildFragment, Observer {
                articleAdapter.setAdapterAnimation(it)
            })
        }
    }

}