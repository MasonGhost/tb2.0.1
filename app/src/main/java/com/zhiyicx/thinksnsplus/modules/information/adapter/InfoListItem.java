package com.zhiyicx.thinksnsplus.modules.information.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.common.utils.SkinUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

public abstract class InfoListItem implements ItemViewDelegate<BaseListBean> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_info;
    }

    @Override
    public boolean isForViewType(BaseListBean item, int position) {
        return item instanceof InfoListDataBean;
    }

    @Override
    public void convert(ViewHolder holder, BaseListBean baseListBean, BaseListBean lastT,
                        final int position, int itemCounts) {
        final InfoListDataBean realData = (InfoListDataBean) baseListBean;
        final TextView title = holder.getView(R.id.item_info_title);
        final ImageView imageView = holder.getView(R.id.item_info_imag);

        // 记录点击过后颜色
        if (AppApplication.sOverRead.contains(position + "")) {
            title.setTextColor(SkinUtils.getColor(R.color.normal_for_assist_text));
        }

        title.setText(realData.getTitle());

        int w = title.getContext().getResources().getDimensionPixelOffset(R.dimen.info_channel_list_image_width);
        int h = title.getContext().getResources().getDimensionPixelOffset(R.dimen.info_channel_list_height);

        if (realData.getImage() == null) {
            imageView.setVisibility(View.GONE);
            if (realData.getUser_id() < 0) {// 广告
                imageView.setVisibility(View.VISIBLE);
                Glide.with(BaseApplication.getContext())
                        .load(realData.getAuthor())
                        .placeholder(R.drawable.shape_default_image)
                        .error(R.drawable.shape_default_image)
                        .override(w, h)
                        .into(imageView);
            }
        } else {
            imageView.setVisibility(View.VISIBLE);
            Glide.with(BaseApplication.getContext())
                    .load(ImageUtils.imagePathConvertV2(realData.getImage().getId(), imageView.getWidth(), imageView.getHeight(),
                            ImageZipConfig.IMAGE_80_ZIP))
                    .placeholder(R.drawable.shape_default_image)
                    .error(R.drawable.shape_default_image)
                    .override(w, h)
                    .into(imageView);
        }
        // 来自单独分开
        String category = realData.getCategory() == null ? "" : realData.getCategory().getName();
        holder.setText(R.id.tv_from_channel, category);
        // 投稿来源，浏览数，时间
        String from = realData.getFrom().equals(title.getContext().getString(R.string.info_publish_original)) ?
                realData.getAuthor() : realData.getFrom();
        String infoData = String.format(title.getContext().getString(R.string.info_list_count)
                , from, realData.getHits(), TimeUtils.getTimeFriendlyNormal(realData
                        .getUpdated_at()));
        holder.setText(R.id.item_info_timeform, infoData);
        // 是否置顶
        holder.setVisible(R.id.tv_top_flag, realData.isTop() ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> itemClick(position, imageView, title, realData));
    }

    public abstract void itemClick(int position, ImageView imageView, TextView title, InfoListDataBean realData);

}