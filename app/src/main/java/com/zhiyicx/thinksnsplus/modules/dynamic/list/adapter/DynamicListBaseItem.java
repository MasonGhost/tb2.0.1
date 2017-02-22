package com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.baseproject.widget.DynamicListMenuView;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @Describe 我发送的文本消息
 * @Author Jungle68
 * @Date 2017/1/6
 * @Contact master.jungle68@gmail.com
 */

public class DynamicListBaseItem implements ItemViewDelegate<DynamicBean> {

    protected ImageLoader mImageLoader;
    protected Context mContext;
    protected OnImageClickListener mOnImageClickListener;
    protected DynamicListMenuView.OnItemClickListener mOnMenuClick;

    public void setImageLoader(ImageLoader imageLoader) {
        mImageLoader = imageLoader;
    }

    public DynamicListBaseItem(Context context) {
        mContext = context;
        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_list;
    }

    @Override
    public boolean isForViewType(DynamicBean item, int position) {
        return false;
    }

    @Override
    public void convert(ViewHolder holder, DynamicBean dynamicBean, DynamicBean lastT, final int position) {
        mImageLoader.loadImage(mContext, GlideImageConfig.builder()
                .url("http://tva2.sinaimg.cn/crop.0.0.1002.1002.50/d710166ajw8fbw38t1do7j20ru0ru0v4.jpg")
                .transformation(new GlideCircleTransform(mContext))
                .errorPic(R.drawable.shape_default_image_circle)
                .imagerView((ImageView) holder.getView(R.id.iv_image))
                .build());
        holder.setText(R.id.tv_name, "张三");
        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(System.currentTimeMillis() / 1000));
        holder.setText(R.id.tv_title, "这个是结构说明投币给大家发给大家");
        holder.setText(R.id.tv_content, "这个是价格肯定就是公开经典福克斯价格开飞机事故雷锋精神的老公就是分开了几个发链接啊过来看对方就是看了感觉法兰克");
        DynamicListMenuView dynamicListMenuView = holder.getView(R.id.dlmv_menu);
        if (mOnMenuClick != null) {
            dynamicListMenuView.setItemOnClick(mOnMenuClick);
        }
    }


    public interface OnImageClickListener {

        void onImageClick(DynamicBean dynamicBean,int position);
    }

}

