package com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @Describe 动态列表 五张图的时候的 item
 * @Author Jungle68
 * @Date 2017/2/22
 * @Contact master.jungle68@gmail.com
 */

public class DynamicListItemForOneImage extends DynamicListBaseItem {


    private static final int IMAGE_COUNTS = 1;// 动态列表图片数量
    private static final int CURREN_CLOUMS = 1; // 当前列数

    public DynamicListItemForOneImage(Context context) {
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
    public void convert(ViewHolder holder, final DynamicBean dynamicBean, DynamicBean lastT, int position) {
        super.convert(holder, dynamicBean, lastT, position);
        initImageView(holder, (ImageView) holder.getView(R.id.siv_0), dynamicBean, 0, 1);
    }

    /**
     * 计算压缩比例
     *
     * @param view
     * @param dynamicBean
     * @param part        占图片显示控件的比例等分
     * @return
     */
    @Override
    protected int getProportion(ImageView view, DynamicBean dynamicBean, int part) {
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
        if ((dynamicBean.getFeed().getStorages() == null || dynamicBean.getFeed().getStorages().size() == DynamicListItemForOneImage.IMAGE_COUNTS)
                && (dynamicBean.getFeed().getLocalPhotos() == null || dynamicBean.getFeed().getLocalPhotos().size() == DynamicListItemForOneImage.IMAGE_COUNTS)) {
            view.setLayoutParams(new LinearLayout.LayoutParams(with, height));
        }
        return  proportion;
    }

    @Override
    protected int getCurrenCloums() {
        return CURREN_CLOUMS;
    }
}

