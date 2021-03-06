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
import kotlinx.android.synthetic.main.include_viewpager.view.*
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import java.lang.StringBuilder

/**
 * ?????????: mmh
 * ?????????: 2021/10/14
 * ?????????: ???????????????????????????????????????
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
 * ??????????????????
 * @param message ?????????????????????????????????
 */
fun LoadService<*>.showError(message: String = "") {
    this.setErrorText(message)
    this.showCallback(ErrorCallback::class.java)
}


/**
 * ???????????????
 */
fun LoadService<*>.showEmpty() {
    this.showCallback(EmptyCallback::class.java)
}

/**
 * ???????????????
 */
fun LoadService<*>.showLoading() {
    this.showCallback(LoadingCallback::class.java)
}

/**
 * ??????????????????
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
        //??????????????????
        when{
            //???????????????????????????????????????
            data.isFirstEmpty -> {
                loadService.showEmpty()
            }
            //????????????
            data.isRefresh ->{
                baseQuickAdapter.setList(data.listData)
                Log.d(TAG, "loadListData: setList????????????")
                loadService.showSuccess()
            }
            //???????????????
            else -> {
                baseQuickAdapter.addData(data.listData)
                loadService.showSuccess()
            }
        }
        //Log.d(TAG, "loadListData: ${baseQuickAdapter.data}")
    } else{
        //??????
        if (data.isRefresh) {
            //??????????????????????????????????????????????????????????????????
            loadService.showError(data.errorMessage)
        } else {
            recyclerView.loadMoreError(0, data.errorMessage)
        }
    }
}




//????????? SwipeRefreshLayout
fun SwipeRefreshLayout.init(onRefreshListener: () -> Unit) {
    this.run {
        setOnRefreshListener {
            onRefreshListener.invoke()
        }
        //??????????????????
        setColorSchemeColors(SettingUtil.getColor(appContext))
    }
}
/*
* ???????????????????????????????????????*/
fun SwipeRecyclerView.initFooter(loadMoreListener: SwipeRecyclerView.LoadMoreListener): DefineLoadMoreView {
    val footerView = DefineLoadMoreView(appContext)
    //?????????????????????
    footerView.setLoadViewColor(SettingUtil.getOneColorStateList(appContext))
    //??????????????????????????????
    //????????????????????????
    footerView.setmLoadMoreListener(SwipeRecyclerView.LoadMoreListener {
        footerView.onLoading()
        loadMoreListener.onLoadMore()
    })
    this.run {
        //????????????????????????
        addFooterView(footerView)
        setLoadMoreView(footerView)
        //????????????????????????
        setLoadMoreListener(loadMoreListener)
    }
    return footerView
}


//???????????????????????????
fun BaseQuickAdapter<*,*>.setAdapterAnimation(mode : Int){
    //??????0?????????????????????
    if (mode == 0) {
        this.animationEnable = false
    } else {
        this.animationEnable = true
        this.setAnimationWithDefault(BaseQuickAdapter.AnimationType.values()[mode - 1])
    }
}


//??????SwipeRecyclerView
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
 * ??????????????????toolbar ???????????????
 */
fun Toolbar.init(titleStr: String = ""): Toolbar {
    setBackgroundColor(SettingUtil.getColor(appContext))
    title = titleStr
    return this
}

/*
* ????????????????????????toolbar*/
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
* ?????????loadSir*/
fun loadServiceInit(view : View, callback: () -> Unit) :LoadService<Any>{
    val loadsir = LoadSir.getDefault().register(view){
        //??????????????????????????????
        callback.invoke()
    }
    loadsir.showSuccess()
    SettingUtil.setLoadingColor(SettingUtil.getColor(appContext), loadsir)
    return loadsir
}

/**
 * ????????????????????????????????????????????????????????????????????? ????????????????????????????????????????????? Textview???FragmentLayout???????????????????????????
 * ???????????????BottomNavigationViewEx????????????????????????FragmentLayout???????????? is Fragmentlayout????????? is BottomNavigationViewEx??????
 * ??????????????????????????? is FragmentLayout???????????? ?????? is BottomNavigationViewEx???????????????
 */

fun setUiTheme(color: Int, vararg anyList: Any?) {
    anyList.forEach { view ->
        view?.let {
            when (it) {
                is LoadService<*> -> SettingUtil.setLoadingColor(color, it as LoadService<Any>)
                is FloatingActionButton -> it.backgroundTintList =
                    SettingUtil.getOneColorStateList(color)
                is SwipeRefreshLayout -> it.setColorSchemeColors(color)
                //???????????????
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


//?????????BottomNavigationViewEx
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





//?????????magic_indicator
//?????????
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
                //????????????
                text = mStringList[index].toHtml()
                //????????????
                textSize = 17f
                //??????????????????
                normalColor = Color.WHITE
                //???????????????
                selectedColor = Color.WHITE
                //??????????????????
                setOnClickListener {
                    viewPage.currentItem = index
                    //invoke????????????????????????
                    action.invoke(index)
                }
            }
        }

        override fun getIndicator(context: Context?): IPagerIndicator {
            return LinePagerIndicator(context).apply {
                mode = LinePagerIndicator.MODE_EXACTLY
                //??????????????????
                lineHeight = UIUtil.dip2px(appContext, 3.0).toFloat()
                lineWidth = UIUtil.dip2px(appContext, 30.0).toFloat()
                //???????????????
                roundRadius = UIUtil.dip2px(appContext, 6.0).toFloat()
                startInterpolator = AccelerateInterpolator()
                endInterpolator = DecelerateInterpolator(2.0f)
                //???????????????
                setColors(Color.WHITE)
            }
        }
    }
    this.navigator = commonNavigator

    //???????????????????????????
    viewPage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            this@bindViewPager2.onPageSelected(position)
            action.invoke(position)
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            this@bindViewPager2.onPageScrolled(position,positionOffset,positionOffsetPixels)
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            this@bindViewPager2.onPageScrollStateChanged(state)
        }
    })
}





//?????????ViewPager2
fun ViewPager2.init(
    fragment: Fragment,
    fragments: ArrayList<Fragment>,
    isUserInputEnabled: Boolean = true
): ViewPager2{
    //??????????????????
    this.isUserInputEnabled = isUserInputEnabled
    //???????????????
    adapter = object : FragmentStateAdapter(fragment){
        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int): Fragment = fragments[position]
    }
    return this
}


//?????????ViewPager2?????????
fun ViewPager2.initMain(fragment: Fragment) : ViewPager2{
    //??????????????????
    this.isUserInputEnabled = false
    this.offscreenPageLimit = 4
    //???????????????
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
 * ???????????????
 */
fun hideSoftKeyboard(activity : Activity?){
    activity?.let { act ->
        //????????????????????????
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