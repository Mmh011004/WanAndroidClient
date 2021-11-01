package com.example.wanandroidclient.ui.fragment.tree

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.jetpackmvvm.ext.nav
import com.example.wanandroidclient.R
import com.example.wanandroidclient.app.appViewModel
import com.example.wanandroidclient.app.base.BaseFragment
import com.example.wanandroidclient.app.ext.bindViewPager2
import com.example.wanandroidclient.app.ext.init
import com.example.wanandroidclient.app.ext.initClose
import com.example.wanandroidclient.data.model.bean.SystemResponse
import com.example.wanandroidclient.databinding.FragmentSystemBinding
import com.example.wanandroidclient.viewmodel.state.TreeViewModel
import kotlinx.android.synthetic.main.include_toolbar.*
import kotlinx.android.synthetic.main.include_viewpager.*

/**
 * 作者　: mmh
 * 时间　: 2021/10/29
 * 描述　: 这个是在体系的条目中点进去后的父Fragment
 */
class SystemArrFragment :BaseFragment<TreeViewModel, FragmentSystemBinding>(){

    lateinit var data : SystemResponse

    var index = 0

    private var fragments : ArrayList<Fragment> = arrayListOf()
    override fun layoutId(): Int {
        return R.layout.fragment_system
    }

    override fun initView(savedInstanceState: Bundle?) {
        //获取前面Bundle传过来的data和index
        arguments?.let {
            //data 是 SystemResponse的数据类型
            data = it.getParcelable("data")!!
            index= it.getInt("index")
        }

        //初始化toolBar
        toolbar.initClose(data.name) {
            nav().navigateUp()
        }

        //初始化顶部栏目的主题颜色
        appViewModel.appColor.value?.let {
            viewpager_linear.setBackgroundColor(it)
        }

        //栏目标题居左显示


    }

    override fun lazyLoadData() {
        //加入各个fragment
        data.children.forEach{
            fragments.add(SystemChildFragment.newInstance(it.id))
        }
        //初始化viewPager2
        view_pager.init(this, fragments)
        //初始化magic_indicator
        //先来一个title集合
        var titleList: ArrayList<String> = arrayListOf()
        titleList.addAll(data.children.map {
            it.name
        })
        magic_indicator.bindViewPager2(view_pager, titleList)

        view_pager.currentItem = index
    }

    override fun createObserver() {

    }
}