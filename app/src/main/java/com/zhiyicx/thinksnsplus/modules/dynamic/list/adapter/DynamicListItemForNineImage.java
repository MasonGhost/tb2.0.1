package com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe 动态列表 五张图的时候的 item
 * @Author Jungle68
 * @Date 2017/2/22
 * @Contact master.jungle68@gmail.com
 */

public class DynamicListItemForNineImage extends DynamicListBaseItem {
    private static final int IMAGE_COUNTS = 9;// 动态列表图片数量

    public DynamicListItemForNineImage(Context context) {
        super(context);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_list_nine_image;
    }

    @Override
    public boolean isForViewType(DynamicBean item, int position) {
        return item.getFeed().getStorage().size() == IMAGE_COUNTS;
    }


    @Override
    public void convert(ViewHolder holder, final DynamicBean dynamicBean, DynamicBean lastT, int position) {
        super.convert(holder, dynamicBean, lastT, position);
        initImageView((ImageView) holder.getView(R.id.siv_0), dynamicBean, 0);
        initImageView((ImageView) holder.getView(R.id.siv_1), dynamicBean, 1);
        initImageView((ImageView) holder.getView(R.id.siv_2), dynamicBean, 2);
        initImageView((ImageView) holder.getView(R.id.siv_3), dynamicBean, 3);
        initImageView((ImageView) holder.getView(R.id.siv_4), dynamicBean, 4);
        initImageView((ImageView) holder.getView(R.id.siv_5), dynamicBean, 5);
        initImageView((ImageView) holder.getView(R.id.siv_6), dynamicBean, 6);
        initImageView((ImageView) holder.getView(R.id.siv_7), dynamicBean, 7);
        initImageView((ImageView) holder.getView(R.id.siv_8), dynamicBean, 8);
    }

    /**
     * 设置 imageview 点击事件，以及显示
     *
     * @param view        the target
     * @param dynamicBean item data
     * @param positon     item position
     */
    private void initImageView(ImageView view, final DynamicBean dynamicBean, final int positon) {
        RxView.clicks(view)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mOnImageClickListener != null) {
                            mOnImageClickListener.onImageClick(dynamicBean, positon);
                        }
                    }
                });
        mImageLoader.loadImage(mContext, GlideImageConfig.builder()
                .url("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1487853874862&di=1783b319156309b01646d42f481e6239&imgtype=0&src=http%3A%2F%2Fa.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F6a63f6246b600c33375d7f6b1f4c510fd9f9a169.jpg") // TODO: 2017/2/22 需要添加图片前最地址dynamicBean.getFeed().getStorage().get(positon)+
                .imagerView(view)
                .build());
    }

}

