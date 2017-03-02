package com.zhiyicx.thinksnsplus.modules.dynamic.detail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleBoundTransform;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.widget.imageview.FilterImageView;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.log.LogUtils;
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
        List<ImageBean> photoList = dynamicDetailBean.getStorages();
        if (photoList != null) {
            for (int i = 0; i < photoList.size(); i++) {
                ImageBean imageBean = photoList.get(i);
                showContentImage(context, imageBean.getStorage_id(), i, i == photoList.size() - 1, photoContainer);
            }
        }
    }

    private void showContentImage(Context context, int storage_id, final int position, boolean lastImg, LinearLayout photoContainer) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_dynamic_detail_photos, null);
        FilterImageView imageView = (FilterImageView) view.findViewById(R.id.dynamic_content_img);
        // 隐藏最后一张图的下间距
        if (lastImg) {
            view.findViewById(R.id.img_divider).setVisibility(View.GONE);
        }
        AppApplication.AppComponentHolder.getAppComponent().imageLoader()
                .loadImage(context, GlideImageConfig.builder()
                        .url(String.format(ApiConfig.IMAGE_PATH, storage_id, 50))
                        .imagerView(imageView)
                        .build()
                );
        photoContainer.addView(view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(position+"");
            }
        });
    }
}
