package com.zhiyicx.thinksnsplus.modules.channel.detail.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe 动态列表 五张图的时候的 item
 * @Author Jungle68
 * @Date 2017/2/22
 * @Contact master.jungle68@gmail.com
 */

public class GroupDynamicListItemForOneImage extends GroupDynamicListBaseItem {


    private static final int IMAGE_COUNTS = 1;// 动态列表图片数量
    private static final int CURREN_CLOUMS = 1; // 当前列数

    public GroupDynamicListItemForOneImage(Context context) {
        super(context);
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
    public void convert(ViewHolder holder, final GroupDynamicListBean dynamicBean, GroupDynamicListBean lastT, int position, int itemCounts) {
        super.convert(holder, dynamicBean, lastT, position, itemCounts);
        initImageView(holder, (ImageView) holder.getView(R.id.siv_0), dynamicBean, 0, 1);
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
    protected void initImageView(final ViewHolder holder, ImageView view, final GroupDynamicListBean dynamicBean, final int positon, int part) {
        /**
         * 一张图时候，需要对宽高做限制
         */
        int with;
        int height;
        int proportion; // 压缩比例
        int currentWith = getCurrenItemWith(part);
        GroupDynamicListBean.ImagesBean imageBean = dynamicBean.getImages().get(0);
        with = currentWith;
        height = (with * imageBean.getHeight() / imageBean.getWidth());
        height = height > mImageMaxHeight ? mImageMaxHeight : height;
        proportion = ((with / imageBean.getWidth()) * 100);
        if (with * height == 0) {// 就怕是 0
            with = height = 100;
        }
        view.setLayoutParams(new LinearLayout.LayoutParams(with, height));

        if (TextUtils.isEmpty(imageBean.getImgUrl())) {
            Boolean canLook = true;
            Glide.with(mContext)
                    .load(ImageUtils.imagePathConvertV2(canLook, imageBean.getFile_id(), with, height, proportion, AppApplication.getTOKEN()))
                    .override(with, height)
                    .placeholder(canLook ? R.drawable.shape_default_image : R.mipmap.pic_locked)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.mipmap.pic_locked)
                    .into(view);
        } else {
            Glide.with(mContext)
                    .load(imageBean.getImgUrl())
                    .override(with, height)
                    .placeholder(R.drawable.shape_default_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.shape_default_image)
                    .into(view);
        }

        if (dynamicBean.getImages() != null) {
            dynamicBean.getImages().get(positon).setPropPart(proportion);
        }
        RxView.clicks(view)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
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

