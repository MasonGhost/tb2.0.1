package com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.widget.imageview.FilterImageView;
import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe 动态列表 五张图的时候的 item
 * @Author Jungle68
 * @Date 2017/2/22
 * @Contact master.jungle68@gmail.com
 */

public class DynamicListItemForOneImage extends DynamicListBaseItem {

    /**
     * 动态列表图片数量
     */
    private static final int IMAGE_COUNTS = 1;
    /**
     * 当前列数
     */
    private static final int CURREN_CLOUMS = 1;

    public DynamicListItemForOneImage(Context context) {
        super(context);
        int maxWidth = context.getResources().getDimensionPixelOffset(R.dimen.dynamic_image_max_width);
        mImageContainerWith = mImageContainerWith > maxWidth ? maxWidth : mImageContainerWith;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_list_one_image;
    }

    @Override
    public int getImageCounts() {
        return IMAGE_COUNTS;
    }


    @Override
    public void convert(ViewHolder holder, final DynamicDetailBeanV2 dynamicBean, DynamicDetailBeanV2 lastT, int position, int itemCounts) {
        super.convert(holder, dynamicBean, lastT, position, itemCounts);
        initImageView(holder, holder.getView(R.id.siv_0), dynamicBean, 0, 1);
    }

    /**
     * 设置 imageview 点击事件，以及显示
     *
     * @param view        the target
     * @param dynamicBean item data
     * @param positon     image item position
     * @param part        this part percent of imageContainer
     */
    @Override
    protected void initImageView(final ViewHolder holder, FilterImageView view, final DynamicDetailBeanV2 dynamicBean, final int positon, int part) {
        int with;
        int height;

        DynamicDetailBeanV2.ImagesBean imageBean = dynamicBean.getImages().get(0);
        if (TextUtils.isEmpty(imageBean.getImgUrl())) {
            with = imageBean.getImageViewWidth();
            height = imageBean.getImageViewHeight();
            // 是否是长图
            view.showLongImageTag(imageBean.hasLongImage());
            view.setLayoutParams(new LinearLayout.LayoutParams(with, height));
            Glide.with(mContext)
                    .load(imageBean.getGlideUrl())
                    .override(with, height)
                    .placeholder(R.drawable.shape_default_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.shape_default_image)
                    .into(view);
        } else {
            // 本地
            BitmapFactory.Options option = DrawableProvider.getPicsWHByFile(imageBean.getImgUrl());
            with = imageBean.getCurrentWith();
            if (option.outWidth == 0) {
                height = with;
            } else {
                height = with * option.outHeight / option.outWidth;
                height = height > mImageMaxHeight ? mImageMaxHeight : height;
                // 是否是长图
                view.showLongImageTag(isLongImage(option.outHeight, option.outWidth));
            }
            if (height < DEFALT_IMAGE_HEIGHT) {
                height = DEFALT_IMAGE_HEIGHT;
            }
            view.setLayoutParams(new LinearLayout.LayoutParams(with, height));

            Glide.with(mContext)
                    .load(imageBean.getImgUrl())
                    .override(with, height)
                    .placeholder(R.drawable.shape_default_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.shape_default_image)
                    .into(view);
        }

        RxView.clicks(view)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (mOnImageClickListener != null) {
                        mOnImageClickListener.onImageClick(holder, dynamicBean, positon);
                    }
                });
    }


    @Override
    protected int getCurrenCloums() {
        return CURREN_CLOUMS;
    }
}

