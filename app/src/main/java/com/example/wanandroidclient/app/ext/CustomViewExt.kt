package com.example.wanandroidclient.app.ext

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.jetpackmvvm.base.appContext
import com.example.wanandroidclient.app.util.SettingUtil
import com.example.wanandroidclient.app.weight.loadCallback.LoadingCallback
import com.example.wanandroidclient.ui.fragment.home.HomeFragment
import com.example.wanandroidclient.ui.fragment.project.ProjectFragment
import com.example.wanandroidclient.ui.fragment.publicNumber.PublicNumberFragment
import com.example.wanandroidclient.ui.fragment.tree.TreeArrFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir

/**
 * 作者　: mmh
 * 时间　: 2021/10/14
 * 描述　: 项目中自定义的视图拓展函数
 */


/**
 * 设置加载中
 */
fun LoadService<*>.showLoading() {
    this.showCallback(LoadingCallback::class.java)
}
/*
* 初始化loadSir*/
fun loadServiceInit(view : View, callback: () -> Unit) :LoadService<Any>{
    val loadsir = LoadSir.getDefault().register(view){
        //点击重试时触发的操作
        callback.invoke()
    }
    loadsir.showSuccess()
    SettingUtil.setLoadingColor(SettingUtil.getColor(appContext), loadsir)
    return loadsir
}

/*

*/
/**
 * 根据控件的类型设置主题，注意，控件具有优先级， 基本类型的控件建议放到最后，像 Textview，FragmentLayout，不然会出现问题，
 * 列如下面的BottomNavigationViewEx他的顶级父控件为FragmentLayout，如果先 is Fragmentlayout判断在 is BottomNavigationViewEx上面
 * 那么就会直接去执行 is FragmentLayout的代码块 跳过 is BottomNavigationViewEx的代码块了
 *//*

fun setUiTheme(color: Int, vararg anyList: Any?) {
    anyList.forEach { view ->
        view?.let {
            when (it) {
                is LoadService<*> -> SettingUtil.setLoadingColor(color, it as LoadService<Any>)
                is FloatingActionButton -> it.backgroundTintList =
                    SettingUtil.getOneColorStateList(color)
                is SwipeRefreshLayout -> it.setColorSchemeColors(color)
                //自定义控件
                is DefineLoadMoreView -> it.setLoadViewColor(SettingUtil.getOneColorStateList(color))
                is BottomNavigationViewEx -> {
                    it.itemIconTintList = SettingUtil.getColorStateList(color)
                    it.itemTextColor = SettingUtil.getColorStateList(color)
                }
                is Toolbar -> it.setBackgroundColor(color)
                is TextView -> it.setTextColor(color)
                is LinearLayout -> it.setBackgroundColor(color)
                is ConstraintLayout -> it.setBackgroundColor(color)
                is FrameLayout -> it.setBackgroundColor(color)
            }
        }
    }
}
*/

//初始化BottomNavigationViewEx
fun BottomNavigationViewEx.init(navigationItemSelectedAction: (Int) -> Unit): BottomNavigationViewEx {
    enableAnimation(true)
    enableShiftingMode(false)
    enableItemShiftingMode(true)
    itemIconTintList = SettingUtil.getColorStateList(SettingUtil.getColor(appContext))
    itemTextColor = SettingUtil.getColorStateList(appContext)
    setTextSize(12F)
    setOnNavigationItemSelectedListener {
        navigationItemSelectedAction.invoke(it.itemId)
        true
    }
    return this
}
//初始化ViewPager2的函数
fun ViewPager2.initMain(fragment: Fragment) : ViewPager2{
    //是否可以滑动
    this.isUserInputEnabled = false
    this.offscreenPageLimit = 4
    //设置适配器
    adapter = object : FragmentStateAdapter(fragment){
        override fun createFragment(position: Int): Fragment {
            when(position){
                0 -> {
                    return HomeFragment()
                }
                1 -> {
                    return ProjectFragment()
                }
                2 -> {
                    return TreeArrFragment()
                }
                3 -> {
                    return PublicNumberFragment()
                }
                else -> {
                    return HomeFragment()
                }
            }
        }
        override fun getItemCount(): Int = 4
    }
    return this
}

/**
 * 隐藏软键盘
 */
fun hideSoftKeyboard(activity : Activity?){
    activity?.let { act ->
        //获取有焦点的视图
        val view = act.currentFocus
        //***
        view?.let {
            val inputMethodManager = act.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}