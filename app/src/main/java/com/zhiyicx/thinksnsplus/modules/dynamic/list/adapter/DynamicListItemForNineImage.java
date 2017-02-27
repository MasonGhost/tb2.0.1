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
        return item.getFeed().getStorage_task_ids() != null &&item.getFeed().getStorage_task_ids().size() == IMAGE_COUNTS;
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

}

