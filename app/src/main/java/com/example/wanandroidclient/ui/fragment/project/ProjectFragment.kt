package com.example.wanandroidclient.ui.fragment.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.wanandroidclient.R
import com.example.wanandroidclient.app.appViewModel
import com.example.wanandroidclient.app.base.BaseFragment
import com.example.wanandroidclient.app.ext.*
import com.example.wanandroidclient.databinding.FragmentViewpagerBinding
import com.example.wanandroidclient.viewmodel.request.RequestProjectViewModel
import com.example.wanandroidclient.viewmodel.state.ProjectViewModel
import com.kingja.loadsir.core.LoadService
import kotlinx.android.synthetic.main.include_viewpager.*


class ProjectFragment : BaseFragment<ProjectViewModel, FragmentViewpagerBinding>() {

    //状态管理者
    private lateinit var loadsir: LoadService<Any>

    //Fragment集合
    var fragments: ArrayList<Fragment> = arrayListOf()

    //标题集合
    var mDataList: ArrayList<String> = arrayListOf()

    private val requestProjectViewModel: RequestProjectViewModel by viewModels()

    override fun layoutId(): Int {
        return R.layout.fragment_viewpager
    }

    override fun initView(savedInstanceState: Bundle?) {
        //状态页配置
        loadsir = loadServiceInit(view_pager) {
            //点击重试的时候触发的操作
            loadsir.showLoading()
            requestProjectViewModel.getProjectTitleData()
        }
        //初始化viewpager2
        view_pager.init(this, fragments)
        //初始化magic_indicator
        magic_indicator.bindViewPager2(view_pager, mDataList)
        appViewModel.appColor.value?.let {
            setUiTheme(it, viewpager_linear, loadsir)
        }

    }
}