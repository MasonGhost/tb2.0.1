package com.zhiyicx.thinksnsplus.modules.dynamic.detail.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.widget.imageview.FilterImageView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * @author LiuChao
 * @describe 动态详情内容item
 * @date 2017/3/1
 * @contact email:450127106@qq.com
 */

public class DynamicDetailItemForContent implements ItemViewDelegate<DynamicBean> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_detail_content;
    }

    @Override
    public boolean isForViewType(DynamicBean item, int position) {
        return position == 0;
    }

    @Override
    public void convert(ViewHolder holder, DynamicBean dynamicBean, DynamicBean lastT, int position) {
        TextView title = holder.getView(R.id.tv_dynamic_title);
        TextView content = holder.getView(R.id.tv_dynamic_content);
        LinearLayout photoContainer = holder.getView(R.id.ll_dynamic_photos_container);

        DynamicDetailBean dynamicDetailBean = dynamicBean.getFeed();
        title.setText(dynamicDetailBean.getTitle());
        content.setText(dynamicDetailBean.getContent());

        Context context = title.getContext();
        // 设置图片
        List<ImageBean> photoList = dynamicDetailBean.getStorage_task_ids();
        if (photoList != null) {
            for (ImageBean imageBean : photoList) {
                showContentImage(context, imageBean.getImgUrl(), photoContainer);
            }
        }
    }

    private void showContentImage(Context context, String url, LinearLayout photoContainer) {
        FilterImageView imageView = new FilterImageView(context);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        AppApplication.AppComponentHolder.getAppComponent().imageLoader()
                .loadImage(context, GlideImageConfig.builder()
                        .url(url)
                        .imagerView(imageView)
                        .build()
                );
        photoContainer.addView(imageView);

    }
}
