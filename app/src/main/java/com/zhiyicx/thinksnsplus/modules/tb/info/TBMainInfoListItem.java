package com.zhiyicx.thinksnsplus.modules.tb.info;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.common.base.BaseApplication;
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

public abstract class TBMainInfoListItem implements ItemViewDelegate<BaseListBean> {

    private boolean mIsShowContent;

    public TBMainInfoListItem(boolean isShowContent) {
        this.mIsShowContent = isShowContent;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_info_tb_main;
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

        RxView.clicks(holder.itemView)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> itemClick(position, imageView, title, realData));

        holder.setText(R.id.item_info_timeform, TimeUtils.getYeayMonthDay(TimeUtils.utc2LocalLong(realData
                .getCreated_at())));

        if (realData.getImage() == null) {
            imageView.setVisibility(View.GONE);
            if (realData.getUser_id() < 0) {// 广告
                imageView.setVisibility(View.VISIBLE);
                Glide.with(BaseApplication.getContext())
                        .load(realData.getAuthor())
                        .placeholder(R.drawable.shape_default_image)
                        .error(R.drawable.shape_default_image)
                        .into(imageView);
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
                    .into(imageView);
        }


    }

    public abstract void itemClick(int position, ImageView imageView, TextView title,
                                   InfoListDataBean realData);

}