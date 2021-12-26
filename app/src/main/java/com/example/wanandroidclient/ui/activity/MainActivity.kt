package com.example.wanandroidclient.ui.activity

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.blankj.utilcode.util.ToastUtils
import com.example.jetpackmvvm.network.manager.NetState
import com.example.wanandroidclient.R
import com.example.wanandroidclient.app.appViewModel
import com.example.wanandroidclient.app.base.BaseActivity
import com.example.wanandroidclient.app.util.StatusBarUtil
import com.example.wanandroidclient.databinding.ActivityMainBinding
import com.example.wanandroidclient.viewmodel.state.MainViewModel
import com.example.wanandroidclient.viewmodel.state.MeViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.include_toolbar.*

class MainActivity : BaseActivity<MeViewModel, ActivityMainBinding>() {

    var exitTime = 0L

    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView(savedInstanceState: Bundle?) {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val nav = Navigation.findNavController(this@MainActivity, R.id.host_fragment)
                if (nav.currentDestination != null && nav.currentDestination!!.id != R.id.mainFragment) {
                    //如果当前界面不是主页，那么直接调用返回即可
                    nav.navigateUp()
                } else {
                    //是主页
                    if (System.currentTimeMillis() - exitTime > 2000) {
                        //Toast.makeText(this@MainActivity,"再按一次退出程序",Toast.LENGTH_SHORT)
                        ToastUtils.showShort("再按一次退出程序")
                        exitTime = System.currentTimeMillis()
                    } else {
                        finish()
                    }
                }
            }
        })
        appViewModel.appColor.value?.let {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            supportActionBar?.setBackgroundDrawable(ColorDrawable(it))
            StatusBarUtil.setColor(this, it, 0) }
        // TODO: 2021/11/3 drawLayout
        /*****/
        setSupportActionBar(toolbar)
        //创建抽屉开关
        /*设置Drawerlayout的开关,并且和Home图标联动*/
        val mToggle = ActionBarDrawerToggle(this, drawer_layout,toolbar,R.string.nav_app_bar_open_drawer_description, 0)
        drawer_layout.addDrawerListener(mToggle)

        /*同步drawerlayout的状态*/
        mToggle.syncState()
    }

    override fun createObserver() {
        appViewModel.appColor.observeInActivity(this, Observer {
            supportActionBar?.setBackgroundDrawable(ColorDrawable(it))
            StatusBarUtil.setColor(this, it, 0)
        })
    }

    override fun onNetworkStateChanged(netState: NetState) {
        super.onNetworkStateChanged(netState)
        if (netState.isSuccess) {
            Toast.makeText(applicationContext,"他妈的终于有网了", Toast.LENGTH_SHORT)
        } else{
            Toast.makeText(applicationContext,"网呢？", Toast.LENGTH_SHORT)
        }
    }

    inner class ProxyClick{
        /*
        * 登录
        * */
        fun login(){}

        /*
        * 收藏*/
        fun collect(){}

        /*
        * 积分
        * */
        fun integral(){}

        /*
        * todo*/
        fun todo(){}

        /*
        * 设置
        * */
        fun setting(){}
    }

}