package com.zhiyicx.thinksnsplus.modules.dynamic.detail.adapter;

import android.app.ActionBar;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.widget.imageview.FilterImageView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicToolBean;
import com.zhiyicx.thinksnsplus.widget.DynamicHorizontalStackIconView;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/28
 * @contact email:450127106@qq.com
 */

public class DynamicDetailItemForDig implements ItemViewDelegate<DynamicBean> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_detail_dig;
    }

    @Override
    public boolean isForViewType(DynamicBean item, int position) {
        return position == 1;
    }

    @Override
    public void convert(ViewHolder holder, DynamicBean dynamicBean, DynamicBean lastT, int position) {

        DynamicHorizontalStackIconView dynamicHorizontalStackIconView = holder.getView(R.id.detail_dig_view);
        DynamicDetailBean dynamicDetailBean = dynamicBean.getFeed();
        DynamicToolBean dynamicToolBean = dynamicBean.getTool();
        dynamicHorizontalStackIconView.setDigCount(dynamicToolBean.getFeed_digg_count());
        dynamicHorizontalStackIconView.setPublishTime(dynamicDetailBean.getCreated_at());
        dynamicHorizontalStackIconView.setViewerCount(dynamicToolBean.getFeed_view_count());
        //dynamicHorizontalStackIconView.setDigUserHeadIcon();
    }


}
