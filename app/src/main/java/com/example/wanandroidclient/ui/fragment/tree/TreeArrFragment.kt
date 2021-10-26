package com.example.wanandroidclient.ui.fragment.tree

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.wanandroidclient.R
import com.example.wanandroidclient.app.appViewModel
import com.example.wanandroidclient.app.base.BaseFragment
import com.example.wanandroidclient.app.ext.bindViewPager2
import com.example.wanandroidclient.app.ext.init
import com.example.wanandroidclient.app.ext.setUiTheme
import com.example.wanandroidclient.app.ext.showLoading
import com.example.wanandroidclient.databinding.FragmentViewpagerBinding
import com.example.wanandroidclient.viewmodel.state.TreeViewModel
import com.kingja.loadsir.core.LoadService
import kotlinx.android.synthetic.main.include_toolbar.*
import kotlinx.android.synthetic.main.include_viewpager.*

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
        //设置toolbar的颜色
        appViewModel.appColor.value?.let { setUiTheme(it,viewpager_linear) }
        include_viewpager_toolbar.run {
            inflateMenu(R.menu.todo_menu)
            // TODO: 2021/10/26  setOnMenuItemClickListener {  }
        }

    }

    override fun lazyLoadData() {
        //懒加载加载控件，数据加载放在各个子fragment中
        view_pager.init(this, fragments).offscreenPageLimit = 5
        magic_indicator.bindViewPager2(view_pager, titleData)
    }


    override fun createObserver() {
        appViewModel.appColor.observeInFragment(this, Observer {
            setUiTheme(it , viewpager_linear)
        })
    }
}