package com.example.wanandroidclient.ui.fragment.project

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.example.wanandroidclient.R
import com.example.wanandroidclient.app.base.BaseFragment
import com.example.wanandroidclient.app.ext.init
import com.example.wanandroidclient.app.ext.loadServiceInit
import com.example.wanandroidclient.app.ext.showLoading
import com.example.wanandroidclient.app.weight.recyclerview.DefineLoadMoreView
import com.example.wanandroidclient.databinding.IncludeListBinding
import com.example.wanandroidclient.ui.adapter.AriticleAdapter
import com.example.wanandroidclient.viewmodel.request.RequestProjectViewModel
import com.example.wanandroidclient.viewmodel.state.ProjectViewModel
import com.kingja.loadsir.core.LoadService
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
        recyclerView

    }

}