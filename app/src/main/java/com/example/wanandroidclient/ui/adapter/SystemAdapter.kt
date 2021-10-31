package com.example.wanandroidclient.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.jetpackmvvm.ext.util.toHtml
import com.example.wanandroidclient.R
import com.example.wanandroidclient.app.ext.setAdapterAnimation
import com.example.wanandroidclient.app.util.SettingUtil
import com.example.wanandroidclient.data.model.bean.SystemResponse
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent


/**
 * 作者　: mmh
 * 时间　: 2021/10/28
 * 描述　: 体系的适配器
 */
class SystemAdapter(data: ArrayList<SystemResponse>) :
    BaseQuickAdapter<SystemResponse, BaseViewHolder>(R.layout.item_system, data){

    private var method: (data : SystemResponse, view : View, position: Int)->Unit = {
        _: SystemResponse, _: View, _: Int ->
    }

    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }


    override fun convert(holder: BaseViewHolder, item: SystemResponse) {
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
            setItemViewCacheSize(60)
            //嵌套滑动不可用
            isNestedScrollingEnabled = false

            //设置子条目的点击事件
            adapter = SystemChildAdapter(item.children).apply {
                setOnItemClickListener { _, view, position ->
                    method(item,view,position)
                }
            }
        }

    }

    fun setChildClick(method: (data: SystemResponse, view: View, position: Int) -> Unit) {
        this.method = method
    }
}