package com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.widget.DynamicListMenuView;
import com.zhiyicx.baseproject.widget.imageview.FilterImageView;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe 动态列表 五张图的时候的 item
 * @Author Jungle68
 * @Date 2017/2/22
 * @Contact master.jungle68@gmail.com
 */

public class DynamicListItemForAdvert extends DynamicListBaseItem {


    private static final int IMAGE_COUNTS = 1;// 动态列表图片数量
    private static final int CURREN_CLOUMS = 1; // 当前列数

    public DynamicListItemForAdvert(Context context) {
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
    public boolean isForViewType(DynamicDetailBeanV2 item, int position) {
        return item.getFeed_from() == -1;
    }

    @Override
    public void convert(ViewHolder holder, final DynamicDetailBeanV2 dynamicBean, DynamicDetailBeanV2 lastT, int position, int itemCounts) {
        super.convert(holder, dynamicBean, lastT, position, itemCounts);
        if (showToolMenu) {
            // 显示工具栏
            DynamicListMenuView dynamicListMenuView = holder.getView(R.id.dlmv_menu);
            dynamicListMenuView.setImageNormalResourceIds(getToolImages());
            dynamicListMenuView.setAdvertItemTextAndStatus("广告", dynamicBean.isHas_digg(), 0);
            dynamicListMenuView.setAdvertItemTextAndStatus(ConvertUtils.numberConvert(dynamicBean
                    .getShare_count()), false, 1);
            dynamicListMenuView.setAdvertItemTextAndStatus(ConvertUtils.numberConvert(dynamicBean
                            .getFeed_view_count() == 0 ? 1 : dynamicBean.getFeed_view_count()),
                    false, 2);// 浏览量没有 0
            // 控制更多按钮的显示隐藏
            dynamicListMenuView.setItemPositionVisiable(0, getVisibleOne());
            dynamicListMenuView.setItemPositionVisiable(1, getVisibleTwo());
            dynamicListMenuView.setItemPositionVisiable(2, getVisibleThree());
            dynamicListMenuView.setItemPositionVisiable(3, getVisibleFour());
            // 设置工具栏的点击事件
            dynamicListMenuView.setItemOnClick((parent, v, menuPostion) -> {
                if (mOnMenuItemClickLisitener != null) {
                    mOnMenuItemClickLisitener.onMenuItemClick(v, position, menuPostion);
                }
            });
        }
        initImageView(holder, holder.getView(R.id.siv_0), dynamicBean, 0, 1);
    }

    /**
     * 设置 imageview 点击事件，以及显示
     *  @param view        the target
     * @param dynamicBean item data
     * @param positon     image item position
     * @param part        this part percent of imageContainer
     */
    @Override
    protected void initImageView(final ViewHolder holder, FilterImageView view, final DynamicDetailBeanV2 dynamicBean, final int positon, int part) {
        /**
         * 一张图时候，需要对宽高做限制
         */
        int with;
        int height;
        int proportion; // 压缩比例
        int currentWith = getCurrenItemWith(part);

        List<DynamicDetailBeanV2.ImagesBean> imageBeanList = dynamicBean.getImages();
        DynamicDetailBeanV2.ImagesBean imageBean = imageBeanList.get(positon);

        with = currentWith;

        proportion = 70;

        String url;
        if (TextUtils.isEmpty(imageBean.getImgUrl())) {
            url = ImageUtils.imagePathConvertV2(imageBean.getFile(), with, with, proportion);
        } else {
            url = imageBean.getImgUrl();
        }
        view.setLayoutParams(new LinearLayout.LayoutParams(with, with));
        Glide.with(view.getContext())
                .load(url)
                .asBitmap()
                .override(with, with)
                .placeholder(R.drawable.shape_default_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.shape_default_image)
                .into(view);
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
    protected int getVisibleThree() {
        return View.GONE;
    }

    @Override
    protected int getVisibleTwo() {
        return View.GONE;
    }

    @Override
    protected int[] getToolImages() {
        return new int[]{
                com.zhiyicx.baseproject.R.mipmap.topbar_close,
                com.zhiyicx.baseproject.R.mipmap.home_ico_comment_normal,
                com.zhiyicx.baseproject.R.mipmap.home_ico_eye_normal,
                com.zhiyicx.baseproject.R.mipmap.home_ico_more
        };
    }

    @Override
    protected int getCurrenCloums() {
        return CURREN_CLOUMS;
    }
}

