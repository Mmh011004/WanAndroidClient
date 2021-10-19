package com.example.wanandroidclient.app.weight.banner

import android.view.View
import com.example.wanandroidclient.R
import com.example.wanandroidclient.data.model.bean.BannerResponse
import com.zhpan.bannerview.BaseBannerAdapter

/**
 * 作者　: mmh
 * 时间　: 2021/10/19
 * 描述　: 首页轮播图的适配器
 */
class HomeBannerAdapter : BaseBannerAdapter<BannerResponse,HomeBannerViewHolder>() {


    override fun createViewHolder(itemView: View, viewType: Int): HomeBannerViewHolder {
        return HomeBannerViewHolder(itemView)
    }

    override fun onBind(
        holder: HomeBannerViewHolder?,
        data: BannerResponse?,
        position: Int,
        pageSize: Int
    ) {
        holder?.bindData(data, position, pageSize)
    }

    override fun getLayoutId(viewType: Int): Int  = R.layout.banner_itemhome

}