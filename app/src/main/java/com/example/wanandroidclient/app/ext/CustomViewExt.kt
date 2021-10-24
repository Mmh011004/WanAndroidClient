package com.example.wanandroidclient.app.ext

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.jetpackmvvm.base.appContext
import com.example.jetpackmvvm.ext.util.toHtml
import com.example.wanandroidclient.R
import com.example.wanandroidclient.app.network.stateCallback.ListDataUiState
import com.example.wanandroidclient.app.util.SettingUtil
import com.example.wanandroidclient.app.weight.loadCallback.EmptyCallback
import com.example.wanandroidclient.app.weight.loadCallback.ErrorCallback
import com.example.wanandroidclient.app.weight.loadCallback.LoadingCallback
import com.example.wanandroidclient.app.weight.recyclerview.DefineLoadMoreView
import com.example.wanandroidclient.app.weight.viewpager.ScaleTransitionPagerTitleView
import com.example.wanandroidclient.ui.fragment.home.HomeFragment
import com.example.wanandroidclient.ui.fragment.project.ProjectFragment
import com.example.wanandroidclient.ui.fragment.publicNumber.PublicNumberFragment
import com.example.wanandroidclient.ui.fragment.tree.TreeArrFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import java.lang.StringBuilder

/**
 * 作者　: mmh
 * 时间　: 2021/10/14
 * 描述　: 项目中自定义的视图拓展函数
 */

const val TAG = "CustomViewExt"
fun LoadService<*>.setErrorText(message: String) {
    if (message.isNotEmpty()) {
        this.setCallBack(ErrorCallback::class.java) { _, view ->
            view.findViewById<TextView>(R.id.error_text).text = message
        }
    }
}

/**
 * 设置错误布局
 * @param message 错误布局显示的提示内容
 */
fun LoadService<*>.showError(message: String = "") {
    this.setErrorText(message)
    this.showCallback(ErrorCallback::class.java)
}


/**
 * 设置空布局
 */
fun LoadService<*>.showEmpty() {
    this.showCallback(EmptyCallback::class.java)
}

/**
 * 设置加载中
 */
fun LoadService<*>.showLoading() {
    this.showCallback(LoadingCallback::class.java)
}

/**
 * 加载列表数据
 */
fun <T> loadListData(
    data : ListDataUiState<T>,
    baseQuickAdapter: BaseQuickAdapter<T,*>,
    loadService : LoadService<*>,
    recyclerView: SwipeRecyclerView,
    swipeRefreshLayout: SwipeRefreshLayout
){
    swipeRefreshLayout.isRefreshing = false
    recyclerView.loadMoreFinish(data.isEmpty,data.hasMore)
    if (data.isSuccess){
        //数据请求成功
        when{
            //第一页没有数据，显示空布局
            data.isFirstEmpty -> {
                loadService.showEmpty()
            }
            //是第一页
            data.isRefresh ->{
                baseQuickAdapter.setList(data.listData)
                Log.d(TAG, "loadListData: setList是否执行")
                loadService.showSuccess()
            }
            //不是第一页
            else -> {
                baseQuickAdapter.addData(data.listData)
                loadService.showSuccess()
            }
        }
        Log.d(TAG, "loadListData: ${baseQuickAdapter.data}")
    } else{
        //失败
        if (data.isRefresh) {
            //如果是第一页，则显示错误界面，并提示错误信息
            loadService.showError(data.errorMessage)
        } else {
            recyclerView.loadMoreError(0, data.errorMessage)
        }
    }
}




//初始化 SwipeRefreshLayout
fun SwipeRefreshLayout.init(onRefreshListener: () -> Unit) {
    this.run {
        setOnRefreshListener {
            onRefreshListener.invoke()
        }
        //设置主题颜色
        setColorSchemeColors(SettingUtil.getColor(appContext))
    }
}
/*
* 初始化底部的加载更多的视图*/
fun SwipeRecyclerView.initFooter(loadMoreListener: SwipeRecyclerView.LoadMoreListener): DefineLoadMoreView {
    val footerView = DefineLoadMoreView(appContext)
    //给尾部设置颜色
    footerView.setLoadViewColor(SettingUtil.getOneColorStateList(appContext))
    //设置加载更多的监听器
    //设置尾部点击回调
    footerView.setmLoadMoreListener(SwipeRecyclerView.LoadMoreListener {
        footerView.onLoading()
        loadMoreListener.onLoadMore()
    })
    this.run {
        //添加加载更多尾部
        addFooterView(footerView)
        setLoadMoreView(footerView)
        //设置加载更多回调
        setLoadMoreListener(loadMoreListener)
    }
    return footerView
}


//设置适配器动画列表
fun BaseQuickAdapter<*,*>.setAdapterAnimation(mode : Int){
    //等于0，关闭动画列表
    if (mode == 0) {
        this.animationEnable = false
    } else {
        this.animationEnable = true
        this.setAnimationWithDefault(BaseQuickAdapter.AnimationType.values()[mode - 1])
    }
}


//绑定SwipeRecyclerView
fun SwipeRecyclerView.init(
    layoutManger: RecyclerView.LayoutManager,
    bindAdapter: RecyclerView.Adapter<*>,
    isScroll: Boolean = true
): SwipeRecyclerView {
    layoutManager = layoutManger
    setHasFixedSize(true)
    adapter = bindAdapter
    isNestedScrollingEnabled = isScroll
    return this
}





/**
 * 初始化普通的toolbar 只设置标题
 */
fun Toolbar.init(titleStr: String = ""): Toolbar {
    setBackgroundColor(SettingUtil.getColor(appContext))
    title = titleStr
    return this
}

/*
* 初始化有返回键的toolbar*/
fun Toolbar.initClose(
    titleStr: String = "",
    backImg : Int = R.drawable.ic_back,
    onBack : (toolbar : Toolbar) -> Unit
) : Toolbar{
    setBackgroundColor(SettingUtil.getColor(appContext))
    title = titleStr.toHtml()
    setNavigationIcon(backImg)
    setNavigationOnClickListener{onBack.invoke(this)}
    return this
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

/**
 * 根据控件的类型设置主题，注意，控件具有优先级， 基本类型的控件建议放到最后，像 Textview，FragmentLayout，不然会出现问题，
 * 列如下面的BottomNavigationViewEx他的顶级父控件为FragmentLayout，如果先 is Fragmentlayout判断在 is BottomNavigationViewEx上面
 * 那么就会直接去执行 is FragmentLayout的代码块 跳过 is BottomNavigationViewEx的代码块了
 */

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

//初始化magic_indicator
fun MagicIndicator.bindViewPager2(
    viewPage: ViewPager2,
    mStringList: List<String> = arrayListOf(),
    action :(index: Int) -> Unit = {}){
    val commonNavigator = CommonNavigator(appContext)
    commonNavigator.adapter = object : CommonNavigatorAdapter(){
        override fun getCount(): Int {
            return mStringList.size
        }

        override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
            return ScaleTransitionPagerTitleView(appContext).apply {
                //设置文本
                text = mStringList[index].toHtml()
                //字体大小
                textSize = 17f
                //未选中的颜色
                normalColor = Color.WHITE
                //选中的颜色
                selectedColor = Color.WHITE
                //设置点击事件
                setOnClickListener {
                    viewPage.currentItem = index
                    //invoke就是实现动态调用
                    action.invoke(index)
                }
            }
        }

        override fun getIndicator(context: Context?): IPagerIndicator {
            return LinePagerIndicator(context).apply {
                mode = LinePagerIndicator.MODE_EXACTLY
                //线条的宽高度
                lineHeight = UIUtil.dip2px(appContext, 3.0).toFloat()
                lineWidth = UIUtil.dip2px(appContext, 30.0).toFloat()
                //线条的圆角
                roundRadius = UIUtil.dip2px(appContext, 6.0).toFloat()
                startInterpolator = AccelerateInterpolator()
                endInterpolator = DecelerateInterpolator(2.0f)
                //线条的颜色
                setColors(Color.WHITE)
            }
        }
    }
    this.navigator = commonNavigator
    // TODO: 2021/10/24 页面切换的回调 
}

//初始化ViewPager2
fun ViewPager2.init(
    fragment: Fragment,
    fragments: ArrayList<Fragment>,
    isUserInputEnabled: Boolean = true
): ViewPager2{
    //是否可以滑动
    this.isUserInputEnabled = isUserInputEnabled
    //设置适配器
    adapter = object : FragmentStateAdapter(fragment){
        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int): Fragment = fragments[position]
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