package com.zhiyicx.thinksnsplus.modules.information.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.SkinUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

public abstract class InfoListItem implements ItemViewDelegate<BaseListBean> {

    private boolean mIsShowContent;

    public InfoListItem(boolean isShowContent) {
        this.mIsShowContent = isShowContent;
    }

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
        if (AppApplication.sOverRead.contains(realData.getId())) {
            title.setTextColor(SkinUtils.getColor(R.color.normal_for_assist_text));
        }

        title.setText(realData.getTitle());

        int w = title.getContext().getResources().getDimensionPixelOffset(R.dimen
                .info_channel_list_image_width);
        int h = title.getContext().getResources().getDimensionPixelOffset(R.dimen
                .info_channel_list_height);
        RxView.clicks(holder.itemView)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> itemClick(position, imageView, title, realData));
        // 被驳回和投稿中只显示内容
        holder.setVisible(R.id.ll_info, mIsShowContent ? View.GONE : View.VISIBLE);
        holder.setVisible(R.id.tv_info_content, mIsShowContent ? View.VISIBLE : View.GONE);
        String content = RegexUtils.replaceImageId(MarkdownConfig.IMAGE_FORMAT, realData.getContent());
        content = content.replaceAll(MarkdownConfig.NORMAL_FORMAT, "");
//        if (TextUtils.isEmpty(content)&&!TextUtils.isEmpty(realData.getSubject())){
//            content = content.replaceAll(realData.getSubject(), "");// 内容中没有摘要了 10.16 16:20
//        }
        holder.setText(R.id.tv_info_content, content);
        // 投稿来源，浏览数，时间
        String from = title.getContext().getString(R.string
                .info_publish_original).equals(realData.getFrom()) ?
                realData.getAuthor() : realData.getFrom();
        String infoData = String.format(title.getContext().getString(R.string.info_list_count)
                , from, ConvertUtils.numberConvert(realData.getHits()), TimeUtils.getTimeFriendlyNormal(realData
                        .getCreated_at()));
        holder.setText(R.id.item_info_timeform, infoData);

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
                holder.setText(R.id.item_info_timeform, TimeUtils.getTimeFriendlyNormal(realData
                        .getCreated_at()));
                holder.itemView.setOnClickListener(v ->
                        CustomWEBActivity.startToWEBActivity(imageView.getContext(), realData
                                        .getUpdated_at()
                                , realData.getTitle())
                );
            }
        } else {
            imageView.setVisibility(View.VISIBLE);
            Glide.with(BaseApplication.getContext())
                    .load(ImageUtils.imagePathConvertV2(realData.getImage().getId(), imageView
                                    .getWidth(), imageView.getHeight(),
                            ImageZipConfig.IMAGE_80_ZIP))
                    .placeholder(R.drawable.shape_default_image)
                    .error(R.drawable.shape_default_image)
                    .override(w, h)
                    .into(imageView);
        }
        // 来自单独分开
        String category = realData.getCategory() == null || (realData.getCategory() != null && realData.getInfo_type() != null && realData.getInfo_type() != -1) ? "" : realData.getCategory().getName();
        holder.setVisible(R.id.tv_from_channel, category.isEmpty() ? View.GONE : View.VISIBLE);
        holder.setText(R.id.tv_from_channel, category);
        // 是否置顶
        holder.setVisible(R.id.tv_top_flag, realData.isTop() ? View.VISIBLE : View.GONE);

    }

    public abstract void itemClick(int position, ImageView imageView, TextView title,
                                   InfoListDataBean realData);

}