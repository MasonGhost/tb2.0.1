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

public class DynamicListItemForThreeImage extends DynamicListBaseItem {
    private static final int IMAGE_COUNTS = 3;// 动态列表图片数量

    public DynamicListItemForThreeImage(Context context) {
        super(context);

    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_list_three_image;
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
                .url("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1487853874863&di=3681a20179e26eb2458df1649e19554b&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F5366d0160924ab1860e1322f37fae6cd7b890b37.jpg") // TODO: 2017/2/22 需要添加图片前最地址dynamicBean.getFeed().getStorage().get(positon)+
                .imagerView(view)
                .build());
    }

}

