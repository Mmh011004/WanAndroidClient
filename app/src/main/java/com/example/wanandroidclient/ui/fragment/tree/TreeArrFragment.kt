package com.example.wanandroidclient.ui.fragment.tree

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.wanandroidclient.R
import com.example.wanandroidclient.app.base.BaseFragment
import com.example.wanandroidclient.databinding.FragmentViewpagerBinding
import com.example.wanandroidclient.viewmodel.state.TreeViewModel

/**
 * 作者　: mmh
 * 时间　: 2021/10/14
 * 描述　:
 */
class TreeArrFragment : BaseFragment<TreeViewModel, FragmentViewpagerBinding>() {

    //标题
    val titleData = arrayListOf("广场", "每日一问", "体系", "导航")
    //Fragment集合
    private var fragments : ArrayList<Fragment> = arrayListOf()

    //添加四个Fragment
    init {
        fragments.add(PlazaFragment())
        fragments.add(AskFragment())
        fragments.add(SystemFragment())
        fragments.add(NavigationFragment())
    }
    override fun layoutId(): Int = R.layout.fragment_viewpager

    override fun initView(savedInstanceState: Bundle?) {

    }
}