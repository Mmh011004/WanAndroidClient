package com.example.wanandroidclient.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.jetpackmvvm.ext.util.toHtml
import com.example.wanandroidclient.R
import com.example.wanandroidclient.app.ext.setAdapterAnimation
import com.example.wanandroidclient.app.util.SettingUtil
import com.example.wanandroidclient.data.model.bean.AriticleResponse
import com.example.wanandroidclient.data.model.bean.NavigationResponse
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

/**
 * 作者　: mmh
 * 时间　: 2021/11/1
 * 描述　: 导航的适配器
 */
class NavigationAdapter(data: MutableList<NavigationResponse>):
    BaseQuickAdapter<NavigationResponse, BaseViewHolder>(R.layout.item_system, data) {

    private var navigationAction: (item: AriticleResponse, view: View) -> Unit =
        { _: AriticleResponse, _: View -> }

    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    override fun convert(holder: BaseViewHolder, item: NavigationResponse) {
        holder.setText(R.id.item_system_title, item.name.toHtml())
        holder.getView<RecyclerView>(R.id.item_system_rv).run {
            val flexboxLayoutManager : FlexboxLayoutManager by lazy {
                FlexboxLayoutManager(context).apply {
                    //方向 主轴为水平方向，起点在左端
                    flexDirection = FlexDirection.ROW
                    //左对齐
                    justifyContent = JustifyContent.FLEX_START
                }
            }
            layoutManager = flexboxLayoutManager
            setHasFixedSize(true)
            //嵌套滑动不可用
            isNestedScrollingEnabled = false

            adapter = NavigationChildAdapter(item.articles).apply {
                setOnItemClickListener { adapter, view, position ->
                    navigationAction(item.articles[position], view)
                }
            }
        }
    }

    fun setNavigationAction(inputNavigationAction: (item: AriticleResponse, view: View)->Unit){
        this.navigationAction = inputNavigationAction
    }


}