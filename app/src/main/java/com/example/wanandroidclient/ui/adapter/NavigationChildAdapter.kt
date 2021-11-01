package com.example.wanandroidclient.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.jetpackmvvm.ext.util.toHtml
import com.example.wanandroidclient.R
import com.example.wanandroidclient.app.ext.setAdapterAnimation
import com.example.wanandroidclient.app.util.ColorUtil
import com.example.wanandroidclient.app.util.SettingUtil
import com.example.wanandroidclient.data.model.bean.AriticleResponse

/**
 * 作者　: mmh
 * 时间　: 2021/11/1
 * 描述　:
 */
class NavigationChildAdapter(data : ArrayList<AriticleResponse>) :
    BaseQuickAdapter<AriticleResponse ,BaseViewHolder>(R.layout.flow_layout, data){

    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    override fun convert(holder: BaseViewHolder, item: AriticleResponse) {
        holder.setText(R.id.flow_tag, item.title.toHtml())
        holder.setTextColor(R.id.flow_tag, ColorUtil.randomColor())
    }

}