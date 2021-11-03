package com.example.wanandroidclient.ui.fragment.publicNumber

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.jetpackmvvm.ext.view.parseState
import com.example.wanandroidclient.R
import com.example.wanandroidclient.app.appViewModel
import com.example.wanandroidclient.app.base.BaseFragment
import com.example.wanandroidclient.app.ext.*
import com.example.wanandroidclient.app.weight.loadCallback.ErrorCallback
import com.example.wanandroidclient.databinding.FragmentViewpagerBinding
import com.example.wanandroidclient.ui.fragment.project.ProjectChildFragment
import com.example.wanandroidclient.viewmodel.request.RequestPublicNumberViewModel
import com.example.wanandroidclient.viewmodel.state.TreeViewModel
import com.kingja.loadsir.core.LoadService
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.android.synthetic.main.include_viewpager.*


class PublicNumberFragment : BaseFragment<RequestPublicNumberViewModel, FragmentViewpagerBinding>() {
    val TAG = "PublicNumberFragment"

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    //Fragment集合
    var fragments: ArrayList<Fragment> = arrayListOf()

    //标题集合
    var mDataList: ArrayList<String> = arrayListOf()

    override fun layoutId(): Int {
        return R.layout.fragment_viewpager
    }

    override fun initView(savedInstanceState: Bundle?) {
        //状态页配置
        loadsir = loadServiceInit(view_pager) {
            loadsir.showLoading()
            mViewModel.getPublicTitleData()
        }
        //初始化viewPager2
        view_pager.init(this, fragments)
        //初始化magic_indicator
        magic_indicator.bindViewPager2(view_pager, mDataList)
        appViewModel.appColor.value?.let {
            setUiTheme(it, viewpager_linear, loadsir)
        }

    }

    override fun lazyLoadData() {
        //页面加载中
        loadsir.showLoading()
        //获取标题数据
        mViewModel.getPublicTitleData()
    }

    override fun createObserver() {
        Log.d(TAG, "createObserver: ")
        mViewModel.titleData.observe(viewLifecycleOwner, Observer { data ->
            parseState(data, {
                mDataList.addAll(it.map { it.name })
                Log.d(TAG, "createObserver: ${mDataList}")
                it.forEach { classify ->
                    fragments.add(PublicChildFragment.newInstance(classify.id)).also { Log.d(TAG, "createObserver: ${classify.id}") }
                    //cid返回成功
                }
                magic_indicator.navigator.notifyDataSetChanged()
                view_pager.adapter?.notifyDataSetChanged()
                view_pager.offscreenPageLimit = fragments.size
                loadsir.showSuccess()
                Log.d(TAG, "createObserver: showSuccess")
            }, {
                //请求项目标题失败
                loadsir.showCallback(ErrorCallback::class.java)
                loadsir.setErrorText(it.errorMsg)
            })
        })
        appViewModel.appColor.observeInFragment(this, Observer {
            setUiTheme(it, viewpager_linear,loadsir)
        })
    }


}