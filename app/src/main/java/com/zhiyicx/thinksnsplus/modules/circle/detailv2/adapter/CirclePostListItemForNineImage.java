package com.zhiyicx.thinksnsplus.modules.circle.detailv2.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author Jliuer
 * @Date 2017/11/30/14:49
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CirclePostListItemForNineImage extends CirclePostListBaseItem {

    private static final int IMAGE_COUNTS = 9;// 动态列表图片数量
    private static final int CURREN_CLOUMS = 3;// 当前列数

    public CirclePostListItemForNineImage(Context context) {
        super(context);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_list_nine_image;
    }

    @Override
    public boolean isForViewType(CirclePostListBean item, int position) {
        boolean isForViewType = item.getId() != null && (item.getImages() != null && item.getImages().size
                () >= getImageCounts());
        return isForViewType;
    }

    @Override
    public int getImageCounts() {
        return IMAGE_COUNTS;
    }

    @Override
    public int getCurrenCloums() {
        return CURREN_CLOUMS;
    }

    @Override
    public void convert(ViewHolder holder, CirclePostListBean circlePostListBean, CirclePostListBean lastT, int position, int itemCounts) {
        super.convert(holder, circlePostListBean, lastT, position, itemCounts);
        initImageView(holder, holder.getView(R.id.siv_0), circlePostListBean, 0, 1); // 数字 0 代表 image 当前的位置， 1 代表他相对与 CURREN_CLOUMS 的份数
        initImageView(holder, holder.getView(R.id.siv_1), circlePostListBean, 1, 1);
        initImageView(holder, holder.getView(R.id.siv_2), circlePostListBean, 2, 1);
        initImageView(holder, holder.getView(R.id.siv_3), circlePostListBean, 3, 1);
        initImageView(holder, holder.getView(R.id.siv_4), circlePostListBean, 4, 1);
        initImageView(holder, holder.getView(R.id.siv_5), circlePostListBean, 5, 1);
        initImageView(holder, holder.getView(R.id.siv_6), circlePostListBean, 6, 1);
        initImageView(holder, holder.getView(R.id.siv_7), circlePostListBean, 7, 1);
        initImageView(holder, holder.getView(R.id.siv_8), circlePostListBean, 8, 1);

        TextView size = holder.getTextView(R.id.tv_numshadow);
        int iamgeSize = circlePostListBean.getImages().size();
        size.setVisibility(iamgeSize > 9 ? View.VISIBLE : View.GONE);
        size.setText("+" + iamgeSize);
        RxView.clicks(size)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> holder.getConvertView().performClick());
    }
}
