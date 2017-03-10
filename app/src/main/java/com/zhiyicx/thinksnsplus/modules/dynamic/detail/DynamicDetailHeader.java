package com.zhiyicx.thinksnsplus.modules.dynamic.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.widget.imageview.FilterImageView;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicToolBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.dig_list.DigListActivity;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.dig_list.DigListFragment;
import com.zhiyicx.thinksnsplus.widget.DynamicHorizontalStackIconView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/10
 * @Contact master.jungle68@gmail.com
 */

public class DynamicDetailHeader {

    private View mDynamicDetailHeader;

    public View getDynamicDetailHeader() {
        return mDynamicDetailHeader;
    }

    public DynamicDetailHeader(Context context) {
        mDynamicDetailHeader = LayoutInflater.from(context).inflate(R.layout.view_header_dynamic_detial, null);
    }

    public void updateHeaderViewData(DynamicBean dynamicBean) {

        TextView title = (TextView) mDynamicDetailHeader.findViewById(R.id.tv_dynamic_title);
        TextView content = (TextView) mDynamicDetailHeader.findViewById(R.id.tv_dynamic_content);
        LinearLayout photoContainer = (LinearLayout) mDynamicDetailHeader.findViewById(R.id.ll_dynamic_photos_container);

        final DynamicDetailBean dynamicDetailBean = dynamicBean.getFeed();
        String titleText = dynamicDetailBean.getTitle();
        if (TextUtils.isEmpty(titleText)) {
            title.setVisibility(View.GONE);
        } else {
            title.setText(titleText);
        }
        String contentText = dynamicDetailBean.getContent();
        if (TextUtils.isEmpty(contentText)) {
            content.setVisibility(View.GONE);
        } else {
            content.setText(contentText);
        }

        final Context context = title.getContext();
        // 设置图片
        List<ImageBean> photoList = dynamicDetailBean.getStorages();
        if (photoList != null) {
            for (int i = 0; i < photoList.size(); i++) {
                ImageBean imageBean = photoList.get(i);
                showContentImage(context, imageBean, i, i == photoList.size() - 1, photoContainer);
            }
        }
        DynamicHorizontalStackIconView dynamicHorizontalStackIconView = (DynamicHorizontalStackIconView) mDynamicDetailHeader.findViewById(R.id.detail_dig_view);
        DynamicToolBean dynamicToolBean = dynamicBean.getTool();
        if (dynamicToolBean == null) {
            return;
        }
        dynamicHorizontalStackIconView.setDigCount(dynamicToolBean.getFeed_digg_count());
        dynamicHorizontalStackIconView.setPublishTime(dynamicDetailBean.getCreated_at());
        dynamicHorizontalStackIconView.setViewerCount(dynamicToolBean.getFeed_view_count());
        // 设置点赞头像
        List<FollowFansBean> userInfoList = dynamicBean.getDigUserInfoList();
        List<ImageBean> imageBeanList = null;
        if (userInfoList != null && !userInfoList.isEmpty()) {
            imageBeanList = new ArrayList<>();
            for (FollowFansBean followFansBean : userInfoList) {
                ImageBean imageBean = new ImageBean();
                imageBean.setStorage_id(TextUtils.isEmpty(followFansBean.getTargetUserInfo().getAvatar()) ? 0 : Integer.parseInt(followFansBean.getTargetUserInfo().getAvatar()));
                imageBeanList.add(imageBean);
            }
        }
        dynamicHorizontalStackIconView.setDigUserHeadIcon(imageBeanList);

        // 设置跳转到点赞列表
        dynamicHorizontalStackIconView.setDigContainerClickListener(new DynamicHorizontalStackIconView.DigContainerClickListener() {
            @Override
            public void digContainerClick(View digContainer) {
                Bundle bundle = new Bundle();
                bundle.putLong(DigListFragment.DIG_LIST_DATA, dynamicDetailBean.getFeed_id());
                Intent intent = new Intent(mDynamicDetailHeader.getContext(), DigListActivity.class);
                intent.putExtras(bundle);
                mDynamicDetailHeader.getContext().startActivity(intent);
            }
        });
    }

    private void showContentImage(Context context, ImageBean imageBean, final int position, boolean lastImg, LinearLayout photoContainer) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_dynamic_detail_photos, null);
        FilterImageView imageView = (FilterImageView) view.findViewById(R.id.dynamic_content_img);
        // 提前设置图片控件的大小，使得占位图显示
        int width = UIUtils.getWindowWidth(context) - context.getResources().getDimensionPixelSize(R.dimen.spacing_normal) * 2;
        int height = (int) (imageBean.getHeight() * width / imageBean.getWidth());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        imageView.setLayoutParams(layoutParams);
        // 隐藏最后一张图的下间距
        if (lastImg) {
            view.findViewById(R.id.img_divider).setVisibility(View.GONE);
        }

        LogUtils.i("content_image-->" + "ImageBean--》" + imageBean.toString()
        );

        AppApplication.AppComponentHolder.getAppComponent().imageLoader()
                .loadImage(context, GlideImageConfig.builder()
                        .placeholder(R.drawable.shape_default_image)
                        .errorPic(R.drawable.shape_default_image)
                        .url(String.format(ApiConfig.IMAGE_PATH, imageBean.getStorage_id(), 50))
                        .imagerView(imageView)
                        .build()
                );
        photoContainer.addView(view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(position + "");
            }
        });
    }
}
