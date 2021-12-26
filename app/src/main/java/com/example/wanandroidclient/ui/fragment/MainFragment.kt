package com.example.wanandroidclient.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.lifecycle.Observer
import com.example.wanandroidclient.R
import com.example.wanandroidclient.app.appViewModel
import com.example.wanandroidclient.app.base.BaseFragment
import com.example.wanandroidclient.app.ext.init
import com.example.wanandroidclient.app.ext.initMain
import com.example.wanandroidclient.databinding.FragmentMainBinding
import com.example.wanandroidclient.viewmodel.state.MainViewModel
import com.example.wanandroidclient.viewmodel.state.MeViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.include_toolbar.*

class MainFragment : BaseFragment<MeViewModel, FragmentMainBinding>() {
    override fun layoutId(): Int {
        return R.layout.fragment_main
    }

    override fun initView(savedInstanceState: Bundle?) {
        //初始化View Pager2
        mainViewpager.initMain(this)
        //初始化bottomBar
        mainBottom.init{
            when (it) {
                R.id.menu_main -> mainViewpager.setCurrentItem(0, false)
                R.id.menu_project -> mainViewpager.setCurrentItem(1, false)
                R.id.menu_system -> mainViewpager.setCurrentItem(2, false)
                R.id.menu_public -> mainViewpager.setCurrentItem(3, false)
            }
        }
        //mainBottom.interceptLongClick(R.id.menu_main,R.id.menu_project,R.id.menu_system,R.id.menu_public)




    }
    override fun createObserver() {
        appViewModel.appColor.observeInFragment(this, Observer {
            //
        })
    }


}