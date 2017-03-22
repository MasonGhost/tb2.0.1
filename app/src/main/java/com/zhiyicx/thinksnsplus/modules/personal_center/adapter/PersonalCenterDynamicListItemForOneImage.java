package com.zhiyicx.thinksnsplus.modules.personal_center.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/10
 * @contact email:450127106@qq.com
 */

public class PersonalCenterDynamicListItemForOneImage extends PersonalCenterDynamicListBaseItem {
    public PersonalCenterDynamicListItemForOneImage(Context context) {
        super(context);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_personal_center_dynamic_list_one_image;
    }

    private static final int IMAGE_COUNTS = 1;// 动态列表图片数量
    private static final int CURREN_CLOUMS = 1; // 当前列数

    @Override
    public int getImageCounts() {
        return IMAGE_COUNTS;
    }

    @Override
    public void convert(ViewHolder holder, final DynamicBean dynamicBean, DynamicBean lastT, int position) {
        super.convert(holder, dynamicBean, lastT, position);
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
    protected void initImageView(final ViewHolder holder, ImageView view, final DynamicBean dynamicBean, final int positon, int part) {
        /**
         * 一张图时候，需要对宽高做限制
         */
        int with;
        int height;
        int proportion; // 压缩比例
        int currentWith = getCurrenItemWith(part);
        if (dynamicBean.getFeed().getStorages() == null || dynamicBean.getFeed().getStorages().size() == 0) {// 本地图片
            BitmapFactory.Options option = DrawableProvider.getPicsWHByFile(dynamicBean.getFeed().getLocalPhotos().get(0));
            with = option.outWidth > currentWith ? currentWith : option.outWidth;
            height = with * option.outHeight / option.outWidth;
            height = height > mImageMaxHeight ? mImageMaxHeight : height;
            proportion = ((with / option.outWidth) * 100);
        } else {
            with = (int) dynamicBean.getFeed().getStorages().get(0).getWidth() > currentWith ? currentWith : (int) dynamicBean.getFeed().getStorages().get(0).getWidth();
            height = (int) (with * dynamicBean.getFeed().getStorages().get(0).getHeight() / dynamicBean.getFeed().getStorages().get(0).getWidth());
            height = height > mImageMaxHeight ? mImageMaxHeight : height;
            proportion = (int) ((with / dynamicBean.getFeed().getStorages().get(0).getWidth()) * 100);
        }
        if ((dynamicBean.getFeed().getStorages() == null || dynamicBean.getFeed().getStorages().size() == IMAGE_COUNTS)
                && (dynamicBean.getFeed().getLocalPhotos() == null || dynamicBean.getFeed().getLocalPhotos().size() == IMAGE_COUNTS)) {
            view.setLayoutParams(new LinearLayout.LayoutParams(with, height));
        }
        String url;
        if (dynamicBean.getFeed().getStorages() != null && dynamicBean.getFeed().getStorages().size() > 0) {
            url = String.format(ApiConfig.IMAGE_PATH, dynamicBean.getFeed().getStorages().get(positon).getStorage_id(), proportion);
        } else {
            url = dynamicBean.getFeed().getLocalPhotos().get(positon);
        }
        Glide.with(mContext)
                .load(url)
                .override(with, height)
                .into(view);
        if (dynamicBean.getFeed().getStorages() != null) {
            dynamicBean.getFeed().getStorages().get(positon).setPart(proportion);
        }
        RxView.clicks(view)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mOnImageClickListener != null) {
                            mOnImageClickListener.onImageClick(holder, dynamicBean, positon);
                        }
                    }
                });
        view.setBackgroundColor(mContext.getResources().getColor(R.color.themeColor));
    }

    @Override
    protected int getCurrenCloums() {
        return CURREN_CLOUMS;
    }
}
