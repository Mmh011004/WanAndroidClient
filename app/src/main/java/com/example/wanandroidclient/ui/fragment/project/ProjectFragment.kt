package com.example.wanandroidclient.ui.fragment.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.wanandroidclient.R
import com.example.wanandroidclient.app.base.BaseFragment
import com.example.wanandroidclient.databinding.FragmentViewpagerBinding
import com.example.wanandroidclient.viewmodel.state.ProjectViewModel
import com.kingja.loadsir.core.LoadService


class ProjectFragment : BaseFragment<ProjectViewModel, FragmentViewpagerBinding>() {

    //状态管理者
    private lateinit var loadsir: LoadService<Any>

    //Fragment集合
    var fragments: ArrayList<Fragment> = arrayListOf()

    //标题集合
    var mDataList: ArrayList<String> = arrayListOf()



    override fun layoutId(): Int {
        return R.layout.fragment_viewpager
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

}