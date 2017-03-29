package com.zhiyicx.thinksnsplus.modules.information.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.utils.ImageUtils;
import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.InfoListBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

public abstract class InfoListItem implements ItemViewDelegate<BaseListBean> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_info;
    }

    @Override
    public boolean isForViewType(BaseListBean item, int position) {
        return item instanceof InfoListBean.ListBean;
    }

    @Override
    public void convert(ViewHolder holder, BaseListBean baseListBean, BaseListBean lastT,
                        final int position) {
        final InfoListBean.ListBean realData = (InfoListBean.ListBean) baseListBean;
        final TextView title = holder.getView(R.id.item_info_title);
        ImageView imageView = holder.getView(R.id.item_info_imag);

        // 记录点击过后颜色
        if (AppApplication.sOverRead.contains(position + "")) {
            title.setTextColor(BaseApplication.getContext().getResources()
                    .getColor(R.color.normal_for_assist_text));
        }
        title.setText(realData.getTitle());
        AppApplication.AppComponentHolder.getAppComponent().imageLoader().loadImage(BaseApplication.getContext(), GlideImageConfig.builder()
                .url(ImageUtils.imagePathConvert(realData.getStorage().getId()+"",ImageZipConfig.IMAGE_50_ZIP))
                .imagerView(imageView)
                .build());

        String from = TextUtils.isEmpty(realData.getFrom()) ? "" : "\b\b\b" + realData.getFrom();
        holder.setText(R.id.item_info_timeform, TimeUtils.getTimeFriendlyNormal(realData
                .getUpdated_at()) + from);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick(position, title, realData);
            }
        });
    }

    public abstract void itemClick(int position, TextView title, InfoListBean.ListBean realData);

}